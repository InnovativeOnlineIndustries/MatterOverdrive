package com.teamacronymcoders.matteroverdrive.item.food;

import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.item.MOItems;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AndroidBluePillItem extends AndroidPillItem {
    public AndroidBluePillItem(Properties properties, TextColor pillColor) {
        super(properties, pillColor);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entityLiving);
        if (!level.isClientSide()) {
            if (entityLiving instanceof Player) {
                Player player = (Player) entityLiving;
                player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(androidData -> {
                    androidData.setAndroid(false);
                    androidData.getPerkManager().getEnabled().clear();
                    androidData.getPerkManager().getOwned().clear();
                    DamageSource fake = new DamageSource("android_transformation");
                    fake.bypassInvul();
                    fake.bypassArmor();
                    player.hurt(fake, Integer.MAX_VALUE);
                });
            } else {
                // In case a fox or something eats it >:)
                entityLiving.hurt(MOItems.NANITES, 30);
            }
        }
        return stack;
    }
}
