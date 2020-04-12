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

package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.gui.element.ElementDualScaled;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineMatterRecycler;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiRecycler extends MOGuiMachine<TileEntityMachineMatterRecycler> {
    MOElementEnergy energyElement;
    ElementDualScaled recycle_progress;
    ElementSlot outputSlot;

    public GuiRecycler(InventoryPlayer inventoryPlayer, TileEntityMachineMatterRecycler machine) {
        super(ContainerFactory.createMachineContainer(machine, inventoryPlayer), machine);

        name = "recycler";
        energyElement = new MOElementEnergy(this, 100, 39, machine.getEnergyStorage());
        recycle_progress = new ElementDualScaled(this, 32, 54);
        outputSlot = new ElementInventorySlot(this, getContainer().getSlotAt(machine.OUTPUT_SLOT_ID), 64, 52, 22, 22, "big");

        recycle_progress.setMode(1);
        recycle_progress.setSize(24, 16);
        recycle_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
        energyElement.setTexture(Reference.TEXTURE_FE_METER, 32, 64);
    }

    @Override
    public void initGui() {
        super.initGui();

        pages.get(0).addElement(outputSlot);
        pages.get(0).addElement(energyElement);
        this.addElement(recycle_progress);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
        recycle_progress.setQuantity(Math.round(((ContainerMachine) getContainer()).getProgress() * 24));
        manageRequirementTooltips();
    }

    void manageRequirementTooltips() {
        if (!machine.getStackInSlot(machine.INPUT_SLOT_ID).isEmpty()) {
            energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
            energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
        } else {
            energyElement.setEnergyRequired(0);
            energyElement.setEnergyRequiredPerTick(0);
        }
    }
}