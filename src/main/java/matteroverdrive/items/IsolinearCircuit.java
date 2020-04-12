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

package matteroverdrive.items;

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class IsolinearCircuit extends MOBaseItem implements IAdvancedModelProvider {
    public static final String[] subItemNames = {"mk1", "mk2", "mk3", "mk4"};

    public IsolinearCircuit(String name) {
        super(name);
        this.setHasSubtypes(true);
    }

    @Override
    public String[] getSubNames() {
        return subItemNames;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs)) {
            list.add(new ItemStack(this, 1, 0));
            list.add(new ItemStack(this, 1, 1));
            list.add(new ItemStack(this, 1, 2));
            list.add(new ItemStack(this, 1, 3));
        }
    }

    /**
     * Gets an holoIcon index based on an item's damage value
     */
	/*@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage)
    {
        int j = MathHelper.clamp(damage, 0, 3);
        return this.icons[j];
    }*/

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
    public String getTranslationKey(ItemStack stack) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, 3);
        return super.getTranslationKey() + "." + subItemNames[i];
    }

    /*@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[subItemNames.length];

        for (int i = 0; i < subItemNames.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + subItemNames[i]);
        }

        this.itemIcon = this.icons[0];
    }*/
}
