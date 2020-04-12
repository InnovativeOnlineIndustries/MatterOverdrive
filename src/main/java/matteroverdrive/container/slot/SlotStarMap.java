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

package matteroverdrive.container.slot;

import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotStarMap extends MOSlot {
    EntityPlayer player;
    TileEntityMachineStarMap starMap;

    public SlotStarMap(TileEntityMachineStarMap starMap, int slot, EntityPlayer player) {
        super(starMap, slot, 0, 0);
        this.player = player;
        this.starMap = starMap;
    }

    @Override
    public boolean isValid(ItemStack itemStack) {
        return starMap.isItemValidForSlot(getSlotIndex(), itemStack, player);
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack itemStack) {
        starMap.onItemPickup(player, itemStack);
        return super.onTake(player, itemStack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return starMap.getPlanet() == null || starMap.getPlanet().isOwner(player);
    }

    @Override
    public void putStack(ItemStack itemStack) {
        starMap.onItemPlaced(itemStack);
        super.putStack(itemStack);
    }
}
