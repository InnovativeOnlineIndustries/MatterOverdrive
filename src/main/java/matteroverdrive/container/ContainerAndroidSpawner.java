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

import matteroverdrive.tile.TileEntityAndroidSpawner;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAndroidSpawner extends ContainerMachine<TileEntityAndroidSpawner> {
    private int spawnedAndroids;

    public ContainerAndroidSpawner(InventoryPlayer playerInventory, TileEntityAndroidSpawner machine) {
        super(playerInventory, machine);
    }

    @Override
    protected void init(InventoryPlayer inventory) {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, spawnedAndroids);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (this.spawnedAndroids != this.machine.getSpawnedCount()) {
                listener.sendWindowProperty(this, 0, this.machine.getMaxSpawnCount());
            }
        }

        this.spawnedAndroids = this.machine.getSpawnedCount();
    }

    @Override
    public boolean enchantItem(EntityPlayer entityPlayer, int action) {
        if (action == 0) {
            machine.removeAllAndroids();
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue) {
        if (slot == 0) {
            spawnedAndroids = newValue;
        }
    }

    public int getSpawnedCount() {
        return spawnedAndroids;
    }
}
