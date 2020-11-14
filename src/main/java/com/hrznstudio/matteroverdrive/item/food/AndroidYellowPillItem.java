package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.matteroverdrive.item.MOItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
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

        }
        return stack;
    }
}
