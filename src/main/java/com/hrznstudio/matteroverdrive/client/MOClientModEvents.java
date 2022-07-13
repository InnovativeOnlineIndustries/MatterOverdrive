package com.hrznstudio.matteroverdrive.client;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.android.perks.BaseAndroidPerk;
import com.hrznstudio.matteroverdrive.android.perks.PerkTree;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.c2s.AndroidPerkTogglePacket;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import com.hrznstudio.matteroverdrive.util.IHasColor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MatterOverdrive.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class MOClientModEvents {

    @SubscribeEvent
    public static void colorHandlerEvent(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> ((IHasColor) stack.getItem()).getColor(tint),
                MOItems.ANDROID_PILL_RED.get(), MOItems.ANDROID_PILL_BLUE.get(), MOItems.ANDROID_PILL_YELLOW.get());
    }


    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        IAndroidPerk.PERKS.forEach((s, perk) -> {
            if (perk.canBeToggled()) {
                KeyMapping keyBinding = new KeyMapping("key.matteroverdrive." + s + ".desc", -1, "key.matteroverdrive.category");
                event.register(keyBinding);
//                TODO switch to using client ticks directly
//                EventManager.forge(TickEvent.ClientTickEvent.class).process(event -> {
//                    if (keyBinding.isPressed()) {
//                        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
//                            perk.onKeyPress(iAndroid, iAndroid.getPerkManager().getLevel(perk), keyBinding.getKey().getKeyCode(), true);
//                        });
//                        PacketHandler.NETWORK.get().sendToServer(new AndroidPerkTogglePacket(s));
//                    }
//                }).subscribe();
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClient() {
        if (PerkTree.NIGHT_VISION instanceof BaseAndroidPerk) {
            PerkTree.NIGHT_VISION.setOnClientKeyPress((iAndroid, integer) -> {
                Minecraft.getInstance().getSoundHandler().play(new SimpleSound(MOSounds.NIGTH_VISION.get(), SoundCategory.PLAYERS, 0.5f, 1f, Minecraft.getInstance().player.getPosition()));
            });
        }
    }
}
