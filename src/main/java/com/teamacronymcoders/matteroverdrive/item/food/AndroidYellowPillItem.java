package com.teamacronymcoders.matteroverdrive.item.food;

import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AndroidYellowPillItem extends AndroidPillItem {
    public AndroidYellowPillItem(Properties properties, TextColor pillColor) {
        super(properties, pillColor);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entityLiving);
        if (!level.isClientSide()) {
            entityLiving.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                iAndroid.getPerkManager().getOwned().clear();
                iAndroid.getPerkManager().getEnabled().clear();
                iAndroid.requestUpdate();
            });
        }
        return stack;
    }
}
