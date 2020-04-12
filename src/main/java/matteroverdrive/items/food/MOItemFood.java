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

package matteroverdrive.items.food;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.internal.ItemModelProvider;
import matteroverdrive.client.ClientUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

/**
 * @author shadowfacts
 */
public class MOItemFood extends ItemFood implements ItemModelProvider {

    private String name;

    public MOItemFood(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.name = name;

        setTranslationKey(Reference.MOD_ID + "." + name);
        setRegistryName(name);

        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
    }

    @Override
    public void initItemModel() {
        if (!getHasSubtypes())
            ClientUtil.registerModel(this, getRegistryName().toString());
        else {
            NonNullList<ItemStack> sub = NonNullList.create();
            getSubItems(CreativeTabs.SEARCH, sub);
            for (ItemStack stack : sub) {
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), new ModelResourceLocation(getRegistryName(), "inventory"));
            }
        }
    }

}
