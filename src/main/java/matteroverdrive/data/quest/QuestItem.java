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

package matteroverdrive.data.quest;

import com.google.gson.JsonObject;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

public class QuestItem {
    ItemStack itemStack;
    int itemAmount;
    int itemDamage;
    String name;
    String mod;
    NBTTagCompound nbtTagCompound;
    boolean ignoreDamage;
    boolean ignoreNBT;

    public QuestItem(JsonObject object) {
        name = MOJsonHelper.getString(object, "id");
        itemAmount = MOJsonHelper.getInt(object, "count", 1);
        itemDamage = MOJsonHelper.getInt(object, "damage", 0);
        mod = MOJsonHelper.getString(object, "mod", null);
        nbtTagCompound = MOJsonHelper.getNbt(object, "nbt", null);
        ignoreDamage = MOJsonHelper.getBool(object, "ignore_damage", false);
        ignoreNBT = MOJsonHelper.getBool(object, "ignore_nbt", false);
    }

    public QuestItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public QuestItem(String name, String mod) {
        this(name, mod, 1, 0, null);
    }

    public QuestItem(String name, String mod, int itemAmount) {
        this(name, mod, itemAmount, 0, null);
    }

    public QuestItem(String name, String mod, int itemAmount, int itemDamage) {
        this(name, mod, itemAmount, itemDamage, null);
    }

    public QuestItem(String name, String mod, int itemAmount, int itemDamage, NBTTagCompound tagCompound) {
        this.name = name;
        this.mod = mod;
        this.itemAmount = itemAmount;
        this.itemDamage = itemDamage;
        this.nbtTagCompound = tagCompound;
    }

    public static QuestItem fromItemStack(ItemStack itemStack) {
        return new QuestItem(itemStack);
    }

    public boolean isModded() {
        return mod != null && !mod.isEmpty();
    }

    public boolean isModPresent() {
        return Loader.isModLoaded(mod);
    }

    public boolean canItemExist() {
        if (isModded()) {
            return isModPresent();
        }
        return true;
    }

    public ItemStack getItemStack() {
        if (isModded() || itemStack == null) {
            Item item = Item.getByNameOrId(name);
            if (item != null) {
                ItemStack itemStack = new ItemStack(item, itemAmount, itemDamage);
                itemStack.setTagCompound(nbtTagCompound);
                return itemStack;
            }

        } else {
            return itemStack;
        }
        return null;
    }

    public boolean matches(ItemStack itemStack) {
        if (this.itemStack != null) {
            return itemStack.getItem().equals(this.itemStack.getItem()) && (ignoreDamage || itemStack.getItemDamage() == this.itemStack.getItemDamage()) && (ignoreNBT || ItemStack.areItemStackTagsEqual(itemStack, this.itemStack));
        } else {
            return itemStack.getItem().getRegistryName().toString().equals(name) && (ignoreDamage || itemDamage == itemStack.getItemDamage()) && (ignoreNBT || (nbtTagCompound == null || nbtTagCompound.equals(itemStack.getTagCompound())));
        }
    }
}
