package com.teamacronymcoders.matteroverdrive.capabilities;

import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.teamacronymcoders.matteroverdrive.api.android.IAndroid;
import com.teamacronymcoders.matteroverdrive.capabilities.android.AndroidData;
import com.teamacronymcoders.matteroverdrive.capabilities.android.AndroidEnergy;
import com.teamacronymcoders.matteroverdrive.capabilities.android.AndroidEnergyProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = MatterOverdrive.MOD_ID)
public class AndroidCapabilityHandler {

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(MatterOverdrive.MOD_ID, "android_data"), new AndroidData());
            event.addCapability(new ResourceLocation(MatterOverdrive.MOD_ID, "android_energy"), new AndroidEnergyProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(original -> {
            event.getEntity().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(future -> {
                future.deserializeNBT(original.serializeNBT());
                future.requestUpdate();
            });
        });
        event.getOriginal().getCapability(CapabilityEnergy.ENERGY).ifPresent(original -> {
            event.getEntity().getCapability(CapabilityEnergy.ENERGY).ifPresent(future -> {
                if (original instanceof AndroidEnergy && future instanceof AndroidEnergy) {
                    ((AndroidEnergy) future).setEnergy(original.getEnergyStored());
                    if (event.getEntity() instanceof ServerPlayer serverPlayer)
                        AndroidEnergy.syncEnergy(serverPlayer);
                }
            });
        });
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        for (Player playerEntity : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
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
        event.getEntity().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(IAndroid::requestUpdate);
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            AndroidEnergy.syncEnergy(serverPlayer);
    }

}
