package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.matteroverdrive.item.MOItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.world.World;

public class AndroidRedPillItem extends AndroidPillItem {
    public AndroidRedPillItem(Properties properties, Color pillColor) {
        super(properties, pillColor);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
        if (!worldIn.isRemote) {
            if(entityLiving instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entityLiving;
            }
            else {
                // In case a fox or something eats it >:)
                entityLiving.attackEntityFrom(MOItems.NANITES, 30);
            }
        }

        return stack;
    }
}
