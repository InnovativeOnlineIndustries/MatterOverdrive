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

package matteroverdrive.items;

import matteroverdrive.blocks.BlockDecorativeColored;
import matteroverdrive.blocks.BlockDecorativeRotated;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DecorativeBlockItem extends ItemBlock {
    public DecorativeBlockItem(Block block) {
        super(block);
        if (block instanceof BlockDecorativeRotated) {
            setHasSubtypes(true);
            setMaxDamage(0);
        } else if (block instanceof BlockDecorativeColored) {
            setHasSubtypes(true);
            setMaxDamage(0);
        }
    }

    @Override
    public int getMetadata(int damage) {
        if (block instanceof BlockDecorativeRotated) {
            return MathHelper.clamp(damage, 0, 1);
        } else if (block instanceof BlockDecorativeColored) {
            return MathHelper.clamp(damage, 0, 15);
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (block instanceof BlockDecorativeColored) {
            return Minecraft.getMinecraft().getBlockColors().colorMultiplier(block.getStateFromMeta(stack.getMetadata()), null, null, renderPass);
        }
        return Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, renderPass);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        if (block instanceof BlockDecorativeRotated) {
            if (itemStack.getItemDamage() == 1) {
                return super.getItemStackDisplayName(itemStack) + " [Rotated]";
            }
        } else if (block instanceof BlockDecorativeColored) {
            return MOStringHelper.translateToLocal("color." + EnumDyeColor.byMetadata(MathHelper.clamp(itemStack.getItemDamage(), 0, ItemDye.DYE_COLORS.length - 1)).getTranslationKey() + " " + super.getItemStackDisplayName(itemStack));
        }
        return super.getItemStackDisplayName(itemStack);
    }
}
