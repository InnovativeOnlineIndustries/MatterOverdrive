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
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementIndicator;
import matteroverdrive.gui.element.ElementSlotsList;
import matteroverdrive.gui.pages.AutoConfigPage;
import matteroverdrive.gui.pages.PageUpgrades;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;

public class MOGuiMachine<T extends MOTileEntityMachine> extends MOGuiBase {
    T machine;
    ElementSlotsList slotsList;
    ElementIndicator indicator;

    public MOGuiMachine(ContainerMachine<T> container, T machine) {
        this(container, machine, 225, 186);
    }

    public MOGuiMachine(ContainerMachine<T> container, T machine, int width, int height) {
        super(container, width, height);
        this.machine = machine;

        indicator = new ElementIndicator(this, 6, ySize - 18);

        slotsList = new ElementSlotsList(this, 5, 52, 80, 200, machine.getInventoryContainer(), 0);
        slotsList.setMargin(5);

        registerPages(container, machine);
    }

    public void registerPages(MOBaseContainer container, T machine) {
        ElementBaseGroup homePage = new ElementBaseGroup(this, 0, 0, xSize, ySize);
        homePage.setName("Home");
        AutoConfigPage configPage = new AutoConfigPage(this, 48, 32, xSize - 76, ySize);
        configPage.setName("Configurations");

        AddPage(homePage, ClientProxy.holoIcons.getIcon("page_icon_home"), MOStringHelper.translateToLocal("gui.tooltip.page.home")).setIconColor(Reference.COLOR_MATTER);
        AddPage(configPage, ClientProxy.holoIcons.getIcon("page_icon_config"), MOStringHelper.translateToLocal("gui.tooltip.page.configurations"));

        boolean hasUpgrades = false;
        for (Slot slot : machine.getInventoryContainer().getSlots()) {
            if (slot instanceof UpgradeSlot) {
                hasUpgrades = true;
            }
        }
        if (hasUpgrades) {
            PageUpgrades upgradesPage = new PageUpgrades(this, 0, 0, xSize, ySize, container);
            upgradesPage.setName("Upgrades");
            AddPage(upgradesPage, ClientProxy.holoIcons.getIcon("page_icon_upgrades"), MOStringHelper.translateToLocal("gui.tooltip.page.upgrades"));
        }

        setPage(0);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.addElement(slotsList);
        this.addElement(indicator);
    }

    @Override
    protected void updateElementInformation() {
        super.updateElementInformation();

        if (machine.isActive()) {
            indicator.setIndication(1);
        } else {
            indicator.setIndication(0);
        }
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed) {

    }

    @Override
    public void ListSelectionChange(String name, int selected) {

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (name != null && !name.isEmpty()) {
            String n = MOStringHelper.translateToLocal("gui." + name + ".name");
            fontRenderer.drawString(n, 11 + xSize / 2 - (fontRenderer.getStringWidth(n) / 2), 7, Reference.COLOR_MATTER.getColor());
        }

        drawElements(0, true);
    }

    public T getMachine() {
        return machine;
    }
}