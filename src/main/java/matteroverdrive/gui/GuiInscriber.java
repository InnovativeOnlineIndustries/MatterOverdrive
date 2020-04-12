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
import matteroverdrive.container.ContainerInscriber;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.gui.element.ElementDualScaled;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiInscriber extends MOGuiMachine<TileEntityInscriber> {
    MOElementEnergy energyElement;
    ElementDualScaled inscribe_progress;
    ElementSlot outputSlot;

    public GuiInscriber(InventoryPlayer inventoryPlayer, TileEntityInscriber machine) {
        super(new ContainerInscriber(inventoryPlayer, machine), machine);
        name = "inscriber";
        energyElement = new MOElementEnergy(this, 100, 39, machine.getEnergyStorage());
        inscribe_progress = new ElementDualScaled(this, 32, 55);
        outputSlot = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityInscriber.OUTPUT_SLOT_ID), 129, 55, 22, 22, "big");

        inscribe_progress.setMode(1);
        inscribe_progress.setSize(24, 16);
        inscribe_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
    }

    @Override
    public void initGui() {
        super.initGui();

        pages.get(0).addElement(outputSlot);
        pages.get(0).addElement(energyElement);
        this.addElement(inscribe_progress);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
        inscribe_progress.setQuantity(Math.round((((ContainerMachine) getContainer()).getProgress() * 24)));
    }
}
