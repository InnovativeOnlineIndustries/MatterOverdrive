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

package matteroverdrive.blocks.includes;

import matteroverdrive.init.MatterOverdriveCapabilities;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MOMatterEnergyStorageBlock<TE extends TileEntity> extends MOBlockMachine<TE> {
    protected boolean dropsItself;
    private boolean keepsMatter;
    private boolean keepsEnergy;

    public MOMatterEnergyStorageBlock(Material material, String name, boolean keepsEnergy, boolean keepsMatter) {
        super(material, name);
        this.keepsEnergy = keepsEnergy;
        this.keepsMatter = keepsMatter;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasTagCompound()) {
            TileEntity entity = worldIn.getTileEntity(pos);

            if (entity.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null)) {
                if (this.keepsMatter) {
                    entity.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).setMatterStored(stack.getTagCompound().getInteger("Matter"));
                }
            }
        }
    }

    /*@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if(dropsItself)
        {
            MOTileEntityMachineMatter tile = (MOTileEntityMachineMatter)world.getTileEntity(x,y,z);

            if (tile != null && !world.isRemote && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
            {
                ItemStack item = new ItemStack(this);

                if(tile.getMatterStored() > 0 && this.keepsMatter)
                {
                    if(!item.hasTagCompound())
                        item.setTagCompound(new NBTTagCompound());

                    item.getTagCompound().setInteger("Matter", tile.getMatterStored());
                }
                if(tile.getEnergyStored(EnumFacing.DOWN) > 0 && this.keepsEnergy)
                {
                    if(!item.hasTagCompound())
                        item.setTagCompound(new NBTTagCompound());

                    item.getTagCompound().setInteger("Energy", tile.getEnergyStored(EnumFacing.DOWN));
                    item.getTagCompound().setInteger("MaxEnergy", tile.getMaxEnergyStored(EnumFacing.DOWN));
                }

                this.dropBlockAsItem(world, x, y, z, item);
            }
        }
        return super.removedByPlayer(world,player,x,y,z);
    }*/

}
