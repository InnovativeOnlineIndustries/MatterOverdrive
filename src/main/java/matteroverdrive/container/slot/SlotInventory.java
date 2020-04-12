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

import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.data.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotInventory extends MOSlot {

    Slot slot;

    public SlotInventory(IInventory inventory, Slot slot, int x, int y) {
        super(inventory, slot.getId(), x, y);
        this.slot = slot;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return slot.isValidForSlot(itemStack);
    }

    public int getSlotStackLimit() {
        return slot.getMaxStackSize();
    }

    public Slot getSlot() {
        return slot;
    }

    public String getUnlocalizedTooltip() {
        return slot.getUnlocalizedTooltip();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public HoloIcon getHoloIcon() {
        return slot.getHoloIcon();
    }

    public void onSlotChanged() {
        super.onSlotChanged();
        slot.onSlotChanged();
    }
}
