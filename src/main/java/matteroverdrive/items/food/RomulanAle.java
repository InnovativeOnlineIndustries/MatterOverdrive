/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items.food;

import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class RomulanAle extends MOItemFood {

    public RomulanAle(String name) {
        super(name, 4, 0.6f, false);
        setAlwaysEdible();
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        if (!(entityLiving instanceof EntityPlayer)) {
            return stack;
        }
        if (!((EntityPlayer) entityLiving).capabilities.isCreativeMode && !worldIn.isRemote) {
            stack.shrink(1);
        }
        if (!MOPlayerCapabilityProvider.GetAndroidCapability(entityLiving).isAndroid()) {
            entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 160, 8));
        }

        if (stack.getCount() > 0) {
            ((EntityPlayer) entityLiving).inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            return stack;
        } else {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.DRINK;
    }
}
