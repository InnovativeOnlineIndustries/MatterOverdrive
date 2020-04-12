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

package matteroverdrive.matter_network.tasks;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.text.DecimalFormat;

public class MatterNetworkTaskStorePattern extends MatterNetworkTask {
    ItemStack itemStack;
    int progress;

    public MatterNetworkTaskStorePattern() {
        super();

    }

    public MatterNetworkTaskStorePattern(ItemStack itemStack, int progress) {
        this.itemStack = itemStack;
        this.progress = progress;
    }

    @Override
    protected void init() {
        setUnlocalizedName("store_pattern");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound != null) {
            itemStack = new ItemStack(compound.getCompoundTag("Item"));
            progress = compound.getInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (compound != null) {
            NBTTagCompound itemComp = new NBTTagCompound();
            if (itemStack != null) {
                itemStack.writeToNBT(itemComp);
            }
            compound.setTag("Item", itemComp);
            compound.setInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME, progress);
        }
    }

    @Override
    public String getName() {
        return itemStack.getDisplayName() + " +" + DecimalFormat.getPercentInstance().format(progress / 100f);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isValid(World world) {
        if (!super.isValid(world)) {
            return false;
        }

        return MatterHelper.getMatterAmountFromItem(itemStack) > 0;
    }

}
