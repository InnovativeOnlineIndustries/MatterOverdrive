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

package matteroverdrive.data;

import matteroverdrive.util.MOInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class ItemInventoryWrapper implements IInventory {
    ItemStack inventory;
    int size;
    boolean dirty;

    public ItemInventoryWrapper(ItemStack itemStack, int size) {
        this.inventory = itemStack;
        this.size = size;
    }

    @Override
    public int getSizeInventory() {
        return size;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return MOInventoryHelper.getStackInSlot(inventory, slot);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slot, int amount) {
        return MOInventoryHelper.decrStackSize(inventory, slot, amount);
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = MOInventoryHelper.getStackInSlot(inventory, index);
        MOInventoryHelper.setInventorySlotContents(inventory, index, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (inventory.getTagCompound() == null) {
            inventory.setTagCompound(new NBTTagCompound());
        }

        MOInventoryHelper.setInventorySlotContents(inventory, slot, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSizeInventory(); i++) {
            removeStackFromSlot(i);
        }
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public String getName() {
        return inventory.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return inventory.getTextComponent();
    }
}
