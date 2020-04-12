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

package matteroverdrive.gui.pages;

import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.element.MOElementTextField;
import matteroverdrive.gui.events.ITextHandler;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;

public class MatterNetworkConfigPage extends AutoConfigPage implements ITextHandler {
    private ComponentMatterNetworkConfigs componentMatterNetworkConfigs;
    private ElementInventorySlot filterSlot;
    private MOElementTextField destinationTextField;

    public MatterNetworkConfigPage(MOGuiMachine gui, int posX, int posY, int width, int height) {
        super(gui, posX, posY, width, height);
        destinationTextField = new MOElementTextField(gui, this, 4, 42, 96, 16);
        destinationTextField.setName("Destination");
        destinationTextField.setBackground(MOElementButton.HOVER_TEXTURE_DARK);
        destinationTextField.setTextOffset(4, 3);
        this.componentMatterNetworkConfigs = gui.getMachine().getComponent(ComponentMatterNetworkConfigs.class);
        filterSlot = new ElementInventorySlot(gui, (MOSlot) machineGui.inventorySlots.getSlot(componentMatterNetworkConfigs.getDestinationFilterSlot()), 104, 37, 22, 22, "big");
    }

    @Override
    public void init() {
        super.init();
        addElement(destinationTextField);
        if (componentMatterNetworkConfigs != null) {
            if (componentMatterNetworkConfigs.getDestinationFilter() != null) {
                destinationTextField.setText(componentMatterNetworkConfigs.getDestinationFilter());
            }
            addElement(filterSlot);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        getFontRenderer().drawString("Destination Address:", posX, posY + 28, 0xFFFFFF);
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed) {
        if (elementName.equals("Destination")) {
            if (componentMatterNetworkConfigs != null) {
                componentMatterNetworkConfigs.setDestinationFilter(text);
                machineGui.getMachine().sendConfigsToServer(false);
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
    }
}
