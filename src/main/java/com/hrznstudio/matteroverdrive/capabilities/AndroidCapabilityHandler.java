package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        for (PlayerEntity playerEntity : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            playerEntity.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.tickClient(Minecraft.getInstance().player));
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.tickClient(Minecraft.getInstance().player));
        }
    }

}
