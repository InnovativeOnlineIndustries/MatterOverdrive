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

import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BionicSlot extends Slot {
    public static final String[] names = {"head", "arms", "legs", "chest", "other", "battery"};
    private final int type;
    public ResourceLocation[] icons = new ResourceLocation[]{,};

    public BionicSlot(boolean isMainSlot, int type) {
        super(isMainSlot);
        this.type = type;
    }

    @Override
    public boolean isValidForSlot(ItemStack item) {
        return item.getItem() instanceof IBionicPart && ((IBionicPart) item.getItem()).getType(item) == type;
    }

    @Override
    public HoloIcon getHoloIcon() {
        if (type < names.length) {
            return ClientProxy.holoIcons.getIcon("android_slot_" + names[type]);
        }
        return null;
    }

    @Override
    public String getUnlocalizedTooltip() {
        if (type < names.length) {
            return String.format("gui.tooltip.slot.bionic.%s", names[type]);
        }
        return null;
    }
}
