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

package matteroverdrive.data.matter_network;

import io.netty.buffer.ByteBuf;
import matteroverdrive.util.MatterDatabaseHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPattern {
    private int itemID;
    private int damage;
    private int progress;

    public ItemPattern() {

    }

    public ItemPattern(ItemStack itemStack) {
        this(itemStack, 0);
    }

    public ItemPattern(ItemStack itemStack, int progress) {
        this(Item.getIdFromItem(itemStack.getItem()), itemStack.getItemDamage(), progress);
    }

    public ItemPattern(int itemID) {
        this(itemID, 0, 0);
    }

    public ItemPattern(int itemID, int damage) {
        this(itemID, damage, 0);
    }

    public ItemPattern(int itemID, int damage, int progress) {
        this.itemID = itemID;
        this.damage = damage;
        this.progress = progress;
    }

    public ItemPattern(NBTTagCompound tagCompound) {
        readFromNBT(tagCompound);
    }

    public static ItemPattern fromBuffer(ByteBuf byteBuf) {
        int itemID = byteBuf.readShort();
        if (itemID < 0) {
            return null;
        } else {
            ItemPattern pattern = new ItemPattern(itemID);
            pattern.progress = byteBuf.readByte();
            pattern.damage = byteBuf.readShort();
            return pattern;
        }
    }

    public static void writeToBuffer(ByteBuf byteBuf, ItemPattern itemPattern) {
        if (itemPattern == null) {
            byteBuf.writeShort(-1);
        } else {
            byteBuf.writeShort(itemPattern.itemID);
            byteBuf.writeByte(itemPattern.progress);
            byteBuf.writeShort(itemPattern.damage);
        }
    }

    public ItemStack toItemStack(boolean withInfo) {
        ItemStack itemStack = new ItemStack(Item.getItemById(itemID));
        itemStack.setItemDamage(damage);
        if (withInfo) {
            itemStack.setTagCompound(new NBTTagCompound());
            itemStack.getTagCompound().setByte(MatterDatabaseHelper.PROGRESS_TAG_NAME, (byte) progress);
        }
        return itemStack;
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setShort("id", (short) itemID);
        nbtTagCompound.setByte(MatterDatabaseHelper.PROGRESS_TAG_NAME, (byte) progress);
        nbtTagCompound.setShort("Damage", (short) damage);
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        itemID = nbtTagCompound.getShort("id");
        progress = nbtTagCompound.getByte(MatterDatabaseHelper.PROGRESS_TAG_NAME);
        damage = nbtTagCompound.getShort("Damage");
    }

    public int getItemID() {
        return itemID;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public float getProgressF() {
        return (float) progress / (float) MatterDatabaseHelper.MAX_ITEM_PROGRESS;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Item getItem() {
        return Item.getItemById(getItemID());
    }

    public boolean equals(ItemStack itemStack) {
        if (itemStack != null) {
            return getDamage() == itemStack.getItemDamage() && getItemID() == Item.getIdFromItem(itemStack.getItem());
        }
        return false;
    }

    public boolean equals(ItemPattern pattern) {
        return this.getItemID() == pattern.getItemID() && this.getDamage() == pattern.getDamage();
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            return true;
        }
        if (object instanceof ItemPattern) {
            return equals((ItemPattern) object);
        }
        return false;
    }

    public String getDisplayName() {
        return toItemStack(false).getDisplayName();
    }

    public ItemPattern copy() {
        ItemPattern pattern = new ItemPattern(itemID, damage, progress);
        return pattern;
    }

    @Override
    public int hashCode() {
        return itemID + damage + progress;
    }
}
