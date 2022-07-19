package com.hrznstudio.matteroverdrive.datagen.client;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.android.perks.PerkTree;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.event.EventManager;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.c2s.AndroidPerkTogglePacket;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import com.hrznstudio.matteroverdrive.util.IHasColor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
public class MOClientModEvents {

    public static void colorHandlerEvent(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> ((IHasColor) stack.getItem()).getColor(tint),
                MOItems.ANDROID_PILL_RED.get(), MOItems.ANDROID_PILL_BLUE.get(), MOItems.ANDROID_PILL_YELLOW.get());
    }

    public static void registerKeyBinds(RegisterKeyMappingsEvent keyMappingsEvent) {
        IAndroidPerk.PERKS.forEach((s, perk) -> {
            if (perk.canBeToggled()) {
                KeyMapping keyBinding = new KeyMapping("key.matteroverdrive." + s + ".desc", -1, "key.matteroverdrive.category");
                keyMappingsEvent.register(keyBinding);
                EventManager.forge(TickEvent.ClientTickEvent.class).process(event -> {
                    if (keyBinding.isDown()) {
                        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                            perk.onKeyPress(iAndroid, iAndroid.getPerkManager().getLevel(perk), keyBinding.getKey().getValue(), true);
                        });
                        PacketHandler.MO_CHANNEL.sendToServer(new AndroidPerkTogglePacket(s));
                    }
                }).subscribe();
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClient() {
        if (PerkTree.NIGHT_VISION != null) {
            PerkTree.NIGHT_VISION.setOnClientKeyPress((iAndroid, integer) -> {
                Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(MOSounds.NIGHT_VISION.get(), SoundSource.PLAYERS, 0.5f, 1f, Minecraft.getInstance().player.level.random, Minecraft.getInstance().player.blockPosition()));
            });
        }
    }
}
