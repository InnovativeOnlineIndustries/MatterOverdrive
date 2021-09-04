package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidDataProvider;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidEnergyProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = MatterOverdrive.MOD_ID)
public class AndroidCapabilityHandler {

    public static void register() {
        CapabilityManager.INSTANCE.register(IAndroid.class, NBTCapabilityStorage.create(CompoundNBT.class), AndroidData::new);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MatterOverdrive.MOD_ID, "android_data"), new AndroidDataProvider());
            event.addCapability(new ResourceLocation(MatterOverdrive.MOD_ID, "android_energy"), new AndroidEnergyProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(original -> {
            event.getPlayer().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(future -> {
                future.deserializeNBT(original.serializeNBT());
                future.requestUpdate();
            });
        });
        event.getOriginal().getCapability(CapabilityEnergy.ENERGY).ifPresent(original -> {
            event.getPlayer().getCapability(CapabilityEnergy.ENERGY).ifPresent(future -> {
                if (original instanceof AndroidEnergyCapability && future instanceof AndroidEnergyCapability) {
                    ((AndroidEnergyCapability) future).setEnergy(original.getEnergyStored());
                    if (event.getPlayer() instanceof ServerPlayerEntity)
                        AndroidEnergyCapability.syncEnergy((ServerPlayerEntity) event.getEntity());
                }
            });
        });
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        for (PlayerEntity playerEntity : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            playerEntity.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.tickServer(playerEntity));
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.tickClient(Minecraft.getInstance().player));
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(IAndroid::requestUpdate);
        if (event.getPlayer() instanceof ServerPlayerEntity)
            AndroidEnergyCapability.syncEnergy((ServerPlayerEntity) event.getEntity());
    }

}
