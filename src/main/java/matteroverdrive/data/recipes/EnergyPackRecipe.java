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

package matteroverdrive.data.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class EnergyPackRecipe extends ShapelessRecipes {
    //TODO: this could probably be turned into a json recipe with custom logic
    public EnergyPackRecipe(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
        super(group, output, ingredients);
    }
/*
	public EnergyPackRecipe(ItemStack... recipeitems)
	{
		super(new ItemStack(MatterOverdrive.items.energyPack), Arrays.asList(recipeitems));
		for (ItemStack stack : recipeItems)
		{
			if (!stack.isEmpty() && stack.getItem() instanceof Battery)
			{

				((Battery)stack.getItem()).setEnergyStored(stack, ((Battery)stack.getItem()).getMaxEnergyStored(stack));
				getRecipeOutput().stackSize = ((Battery)stack.getItem()).getEnergyStored(stack) / MatterOverdrive.items.energyPack.getEnergyAmount(getRecipeOutput());
			}
		}
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
	{
		ItemStack stack = getRecipeOutput().copy();
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++)
		{
			if (inventoryCrafting.getStackInSlot(i) != null && inventoryCrafting.getStackInSlot(i).getItem() instanceof IEnergyContainerItem)
			{
				int energyStored = ((IEnergyContainerItem)inventoryCrafting.getStackInSlot(i).getItem()).getEnergyStored(inventoryCrafting.getStackInSlot(i));
				int packEnergy = MatterOverdrive.items.energyPack.getEnergyAmount(inventoryCrafting.getStackInSlot(i));
				if (energyStored > 0)
				{
					stack.getCount() = energyStored / packEnergy;
					return stack;
				}
			}
		}
		return null;
	}*/
}
