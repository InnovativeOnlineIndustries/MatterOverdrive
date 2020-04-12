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

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockFusionReactorController extends MOBlockMachine<TileEntityMachineFusionReactorController> {
    public BlockFusionReactorController(Material material, String name) {
        super(material, name);
        setHardness(30.0F);
        this.setResistance(10.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
        lightValue = 10;
        setRotationType(MOBlockHelper.RotationType.SIX_WAY);
        setHasRotation();
    }

   /* @SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            return MatterOverdriveIcons.Monitor_back;
        }
        else if (side == MOBlockHelper.getRightSide(meta) || side == MOBlockHelper.getLeftSide(meta))
        {
            return MatterOverdrive.blocks.decomposer.iconTop;
        }
        else
        {
            return MatterOverdriveIcons.YellowStripes;
        }
    }*/

    @Override
    public Class<TileEntityMachineFusionReactorController> getTileEntityClass() {
        return TileEntityMachineFusionReactorController.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachineFusionReactorController();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMachineFusionReactorController) {
            if (((TileEntityMachineFusionReactorController) tileEntity).isValidStructure()) {
                return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
            }
        }
        return false;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        super.onConfigChanged(config);
        TileEntityMachineFusionReactorController.ENERGY_STORAGE = config.getMachineInt(getTranslationKey(), "storage.energy", 100000000, String.format("How much energy can the %s hold", getLocalizedName()));
        TileEntityMachineFusionReactorController.MATTER_STORAGE = config.getMachineInt(getTranslationKey(), "storage.matter", 2048, String.format("How much matter can the %s hold", getLocalizedName()));
        TileEntityMachineFusionReactorController.ENERGY_PER_TICK = config.getMachineInt(getTranslationKey(), "output.energy", 2048, "The Energy Output per tick. Dependant on the size of the anomaly as well");
        TileEntityMachineFusionReactorController.MATTER_DRAIN_PER_TICK = (float) config.getMachineDouble(getTranslationKey(), "drain.matter", 1D / 80D, "How much matter is drained per tick. Dependant on the size of the anomaly as well");
        TileEntityMachineFusionReactorController.MAX_GRAVITATIONAL_ANOMALY_DISTANCE = config.getMachineInt(getTranslationKey(), "distance.anomaly", 3, "The maximum distance of the anomaly");
        TileEntityMachineFusionReactorController.STRUCTURE_CHECK_DELAY = config.getMachineInt(getTranslationKey(), "check.delay", 40, "The time delay between each structure check");
    }
}