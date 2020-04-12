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
import matteroverdrive.tile.TileEntityMachineNetworkSwitch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static matteroverdrive.util.MOBlockHelper.RotationType;

public class BlockNetworkSwitch extends MOBlockMachine<TileEntityMachineNetworkSwitch> {
    public BlockNetworkSwitch(Material material, String name) {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setRotationType(RotationType.PREVENT);
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registrar)
    {
        activeIcon = registrar.registerIcon(this.getTextureName() + "_active");
        this.blockIcon = registrar.registerIcon(this.getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta)
    {
        TileEntity entity = world.getTileEntity(x,y,z);
        if (entity instanceof TileEntityMachineNetworkSwitch)
        {
            if (((TileEntityMachineNetworkSwitch) entity).isActive())
            {
                return activeIcon;
            }
        }
        return blockIcon;
    }*/

    @Override
    public Class<TileEntityMachineNetworkSwitch> getTileEntityClass() {
        return TileEntityMachineNetworkSwitch.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
        return new TileEntityMachineNetworkSwitch();
    }
}
