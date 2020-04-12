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
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.container.ContainerFusionReactor;
import matteroverdrive.gui.element.ElementDoubleCircleBar;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.InventoryPlayer;

import java.text.DecimalFormat;

public class GuiFusionReactor extends MOGuiMachine<TileEntityMachineFusionReactorController> {
    ElementDoubleCircleBar powerBar;
    DecimalFormat format;

    public GuiFusionReactor(InventoryPlayer inventoryPlayer, TileEntityMachineFusionReactorController machine) {
        super(new ContainerFusionReactor(inventoryPlayer, machine), machine, 256, 230);
        format = new DecimalFormat("#.###");
        name = "fusion_reactor";
        powerBar = new ElementDoubleCircleBar(this, 70, 40, 135, 135, Reference.COLOR_GUI_ENERGY);
        powerBar.setColorRight(Reference.COLOR_HOLO);
    }

    @Override
    public void initGui() {
        super.initGui();
        pages.get(0).addElement(powerBar);
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void updateElementInformation() {
        super.updateElementInformation();

        IMatterHandler storage = machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null);
        powerBar.setProgressRight((float) storage.getMatterStored() / (float) storage.getCapacity());
        powerBar.setProgressLeft((float) machine.getEnergyStorage().getEnergyStored() / (float) machine.getEnergyStorage().getMaxEnergyStored());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (pages.get(0).isVisible()) {
            String info = "Efficiency";
            int width = fontRenderer.getStringWidth(info);
            fontRenderer.drawString(info, 140 - width / 2, 132, Reference.COLOR_GUI_DARKER.getColor());
            info = Math.round(machine.getEnergyEfficiency() * 100) + "%";
            width = fontRenderer.getStringWidth(info);
            fontRenderer.drawString(info, 140 - width / 2, 142, Reference.COLOR_GUI_DARKER.getColor());

            double angle = -(Math.PI * 0.87) * powerBar.getProgressLeft() - ((Math.PI * 2) * 0.03);
            int xPos = 137 + (int) Math.round(Math.sin(angle) * 76);
            int yPos = 104 + (int) Math.round(Math.cos(angle) * 74);
            drawCenteredString(fontRenderer, format.format(powerBar.getProgressLeft() * 100) + "%", xPos, yPos, Reference.COLOR_HOLO_RED.getColor());

            angle = (Math.PI * 0.87) * powerBar.getProgressRight() + ((Math.PI * 2) * 0.03);
            xPos = 137 + (int) Math.round(Math.sin(angle) * 76);
            yPos = 104 + (int) Math.round(Math.cos(angle) * 74);
            drawCenteredString(fontRenderer, format.format(powerBar.getProgressRight() * 100) + "%", xPos, yPos, Reference.COLOR_MATTER.getColor());

            info = "+" + ((ContainerFusionReactor) getContainer()).getEnergyPerTick() + MOEnergyHelper.ENERGY_UNIT + "/t";
            width = fontRenderer.getStringWidth(info);
            xPos = 140 - width / 2;
            yPos = 110;
            fontRenderer.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_HOLO_RED.getColor());

            info = "-" + format.format(machine.getMatterDrainPerTick()) + MatterHelper.MATTER_UNIT + "/t";
            width = fontRenderer.getStringWidth(info);
            xPos = 140 - width / 2;
            yPos = 98;
            fontRenderer.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_MATTER.getColor());
        }
    }
}
