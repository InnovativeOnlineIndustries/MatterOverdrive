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

package matteroverdrive.container;

import matteroverdrive.Reference;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAndroidStation extends ContainerMachine<TileEntityAndroidStation> {
    public ContainerAndroidStation(InventoryPlayer playerInventory, TileEntityAndroidStation machine) {
        super(playerInventory, machine);
    }

    @Override
    protected void init(InventoryPlayer inventory) {
        AndroidPlayer android = MOPlayerCapabilityProvider.GetAndroidCapability(inventory.player);

        for (int i = 0; i < Reference.BIONIC_OTHER + 1; i++) {
            addSlotToContainer(new SlotInventory(android, android.getInventory().getSlot(i), 0, 0));
        }
        addSlotToContainer(new SlotEnergy(android.getInventory(), Reference.BIONIC_BATTERY, 8, 55));

        addUpgradeSlots(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
    }
}
