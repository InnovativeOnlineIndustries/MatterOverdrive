package com.hrznstudio.matteroverdrive.client;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.android.perks.BaseAndroidPerk;
import com.hrznstudio.matteroverdrive.android.perks.PerkTree;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.client.AndroidPerkTogglePacket;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MatterOverdrive.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class MOClientModEvents {

    @SubscribeEvent
    public static void colorHandlerEvent(ColorHandlerEvent.Item event) {
        ItemColors itemColorRegistry = event.getItemColors();

        itemColorRegistry.register((stack, tint) -> ((IHasColor) stack.getItem()).getColor(tint),
                MOItems.ANDROID_PILL_RED.get(), MOItems.ANDROID_PILL_BLUE.get(), MOItems.ANDROID_PILL_YELLOW.get());
    }


    @OnlyIn(Dist.CLIENT)
    public static void onClient() {
        IAndroidPerk.PERKS.forEach((s, perk) -> {
            if (perk.canBeToggled()) {
                KeyBinding keyBinding = new KeyBinding("key.matteroverdrive." + s + ".desc", -1, "key.matteroverdrive.category");
                ClientRegistry.registerKeyBinding(keyBinding);
                EventManager.forge(TickEvent.ClientTickEvent.class).process(event -> {
                    if (keyBinding.isPressed()) {
                        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                            perk.onKeyPress(iAndroid, iAndroid.getPerkManager().getLevel(perk), keyBinding.getKey().getKeyCode(), true);
                        });
                        PacketHandler.NETWORK.get().sendToServer(new AndroidPerkTogglePacket(s));
                    }
                }).subscribe();
            }
        });
        if (PerkTree.NIGHT_VISION instanceof BaseAndroidPerk) {
            PerkTree.NIGHT_VISION.setOnClientKeyPress((iAndroid, integer) -> {
                Minecraft.getInstance().getSoundHandler().play(new SimpleSound(MOSounds.NIGTH_VISION.get(), SoundCategory.PLAYERS, 0.5f, 1f, Minecraft.getInstance().player.getPosition()));
            });
        }
    }
}
