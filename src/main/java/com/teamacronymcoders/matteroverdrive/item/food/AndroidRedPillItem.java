package com.teamacronymcoders.matteroverdrive.item.food;

import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.capabilities.android.AndroidData;
import com.teamacronymcoders.matteroverdrive.item.MOItems;
import net.minecraft.network.chat.TextColor;
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
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        ItemStack itemstack = super.finishUsingItem(stack, worldIn, entityLiving);
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
