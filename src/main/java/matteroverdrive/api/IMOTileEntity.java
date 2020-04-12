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

package matteroverdrive.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Simeon
 * @since 3/6/2015
 * Implemented by all Matter overdrive Tile Entities
 */
public interface IMOTileEntity {
    void onAdded(World world, BlockPos pos, IBlockState state);

    void onPlaced(World world, EntityLivingBase entityLiving);

    void onDestroyed(World worldIn, BlockPos pos, IBlockState state);

    void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock);

    void writeToDropItem(ItemStack itemStack);

    void readFromPlaceItem(ItemStack itemStack);

}
