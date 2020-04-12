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

package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.decomposer.TileEntityMachineDecomposer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockDecomposer extends MOMatterEnergyStorageBlock<TileEntityMachineDecomposer> {
    public BlockDecomposer(Material material, String name) {
        super(material, name, true, true);
        setHasRotation();
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public Class<TileEntityMachineDecomposer> getTileEntityClass() {
        return TileEntityMachineDecomposer.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachineDecomposer();
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        super.onConfigChanged(config);
        config.initMachineCategory(getTranslationKey());
        TileEntityMachineDecomposer.MATTER_STORAGE = config.getMachineInt(getTranslationKey(), "storage.matter", 1024, String.format("How much matter can the %s hold", getLocalizedName()));
        TileEntityMachineDecomposer.ENERGY_STORAGE = config.getMachineInt(getTranslationKey(), "storage.energy", 512000, String.format("How much energy can the %s hold", getLocalizedName()));
        TileEntityMachineDecomposer.DECEOPOSE_SPEED_PER_MATTER = config.getMachineInt(getTranslationKey(), "speed.decompose", 80, "The speed in ticks, of decomposing. (per matter)");
        TileEntityMachineDecomposer.DECOMPOSE_ENERGY_PER_MATTER = config.getMachineInt(getTranslationKey(), "cost.decompose", 6000, "Decomposing cost per matter");

    }

}
