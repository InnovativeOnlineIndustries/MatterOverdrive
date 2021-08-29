package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.world.World;

public class AndroidYellowPillItem extends AndroidPillItem {
    public AndroidYellowPillItem(Properties properties, Color pillColor) {
        super(properties, pillColor);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
        if (!worldIn.isRemote) {
            entityLiving.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                iAndroid.getPerkManager().getOwned().clear();
                iAndroid.getPerkManager().getEnabled().clear();
                iAndroid.requestUpdate();
            });
        }
        return stack;
    }
}
