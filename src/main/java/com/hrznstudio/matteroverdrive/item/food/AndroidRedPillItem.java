package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.item.MOItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.text.Color;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AndroidRedPillItem extends AndroidPillItem {
    public AndroidRedPillItem(Item.Properties properties, TextColor pillColor) {
        super(properties, pillColor);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
        if (!worldIn.isClientSide) {
            if (entityLiving instanceof Player) {
                Player player = (Player) entityLiving;
                player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(androidData -> {
                    androidData.setTurningTime(AndroidData.TURNING_TIME);
                });
            } else {
                // In case a fox or something eats it >:)
                entityLiving.hurt(MOItems.NANITES, 30);
            }
        }

        return itemstack;
    }
}
