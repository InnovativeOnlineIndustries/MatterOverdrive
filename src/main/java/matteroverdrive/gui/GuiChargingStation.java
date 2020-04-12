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
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineChargingStation;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiChargingStation extends MOGuiMachine<TileEntityMachineChargingStation> {
    MOElementEnergy energy;

    public GuiChargingStation(InventoryPlayer inventoryPlayer, TileEntityMachineChargingStation chargingStation) {
        super(ContainerFactory.createMachineContainer(chargingStation, inventoryPlayer), chargingStation);
        name = "charging_station";
        energy = new MOElementEnergy(this, 80, 40, chargingStation.getEnergyStorage());
        energy.setTexture(Reference.TEXTURE_FE_METER, 32, 64);
    }

    @Override
    public void initGui() {
        super.initGui();
        pages.get(0).addElement(energy);
        AddMainPlayerSlots(inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (pages.get(0).isVisible()) {
            fontRenderer.drawString(String.format("Range: %s", machine.getRage()), 100, 50, Reference.COLOR_HOLO.getColor());
            fontRenderer.drawString(String.format("Charge Rate: %s", machine.getMaxCharging()), 100, 62, Reference.COLOR_HOLO.getColor());
        }
    }
}
