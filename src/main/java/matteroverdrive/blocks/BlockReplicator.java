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
import matteroverdrive.machines.replicator.ComponentTaskProcessingReplicator;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockReplicator extends MOMatterEnergyStorageBlock<TileEntityMachineReplicator> {
    public float replication_volume;
    public boolean hasVentParticles;

    public BlockReplicator(Material material, String name) {
        super(material, name, true, true);
        setHasRotation();
        setHardness(20.0F);
        setLightOpacity(2);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public Class<TileEntityMachineReplicator> getTileEntityClass() {
        return TileEntityMachineReplicator.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachineReplicator();
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        super.onConfigChanged(config);
        replication_volume = (float) config.getMachineDouble(getTranslationKey(), "volume.replicate", 1, "The volume of the replication animation");
        hasVentParticles = config.getMachineBool(getTranslationKey(), "particles.vent", true, "Should vent particles be displayed");
        TileEntityMachineReplicator.MATTER_STORAGE = config.getMachineInt(getTranslationKey(), "storage.matter", 1024, "How much matter can the replicator hold");
        TileEntityMachineReplicator.ENERGY_STORAGE = config.getMachineInt(getTranslationKey(), "storage.energy", 512000, "How much energy can the replicator hold");
        ComponentTaskProcessingReplicator.REPLICATE_ENERGY_PER_MATTER = config.getMachineInt(getTranslationKey(), "cost.replication.energy", 16000, "The total replication cost of each matter value. The energy cost is calculated like so: (matterAmount*EnergyCost)");
        ComponentTaskProcessingReplicator.REPLICATE_SPEED_PER_MATTER = config.getMachineInt(getTranslationKey(), "speed.replication", 120, "The replication speed in ticks per matter value");
    }
}
