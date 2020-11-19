package com.hrznstudio.matteroverdrive.client;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.titanium.api.material.IHasColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

}
