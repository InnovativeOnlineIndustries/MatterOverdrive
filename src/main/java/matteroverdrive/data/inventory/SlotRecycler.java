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

package matteroverdrive.data.inventory;

import matteroverdrive.api.matter.IRecyclable;
import net.minecraft.item.ItemStack;

public class SlotRecycler extends Slot {
    public SlotRecycler(boolean isMainSlot) {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item) {
        return item.getItem() instanceof IRecyclable && ((IRecyclable) item.getItem()).canRecycle(item);
    }

    @Override
    public String getUnlocalizedTooltip() {
        return "gui.tooltip.slot.recycle";
    }
}
