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

import matteroverdrive.container.matter_network.ContainerTaskQueueMachine;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerReplicator extends ContainerTaskQueueMachine<TileEntityMachineReplicator> {
    private int patternReplicateCount;

    public ContainerReplicator(InventoryPlayer inventory, TileEntityMachineReplicator machine) {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory) {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 1, this.machine.getTaskReplicateCount());
        listener.sendWindowProperty(this, 2, this.machine.getEnergyDrainPerTick());
        listener.sendWindowProperty(this, 3, this.machine.getEnergyDrainMax());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.patternReplicateCount != this.machine.getTaskReplicateCount()) {
                listener.sendWindowProperty(this, 1, this.machine.getTaskReplicateCount());
            }
        }

        this.patternReplicateCount = this.machine.getTaskReplicateCount();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue) {
        super.updateProgressBar(slot, newValue);
        switch (slot) {
            case 1:
                patternReplicateCount = newValue;
                break;
        }
    }

    public int getPatternReplicateCount() {
        return patternReplicateCount;
    }
}
