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

package matteroverdrive.gui.pages.starmap;

import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.gui.element.starmap.ElementQuadrantEntry;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.tile.TileEntityMachineStarMap;

public class PageGalaxy extends ElementBaseGroup implements IListHandler {
    private static int scroll;
    private TileEntityMachineStarMap starMap;
    private ElementGroupList quadrantList;

    public PageGalaxy(GuiStarMap gui, int posX, int posY, int width, int height, TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        this.starMap = starMap;
        quadrantList = new ElementGroupList(gui, this, 16, 16, 0, 0);
        quadrantList.setName("Quadrants");
    }

    private void loadStars() {
        quadrantList.init();
        for (Quadrant quadrant : GalaxyClient.getInstance().getTheGalaxy().getQuadrants()) {
            quadrantList.addElement(new ElementQuadrantEntry((GuiStarMap) gui, quadrantList, 128 + 64, 32, quadrant));

            if (starMap.getDestination().equals(quadrant)) {
                quadrantList.setSelectedIndex(quadrantList.getElements().size() - 1);
            }
        }
        quadrantList.limitScroll();
    }

    @Override
    public void init() {
        super.init();
        quadrantList.setSize(sizeX, sizeY - 100 - 32);
        quadrantList.setScroll(scroll);
        quadrantList.resetSmoothScroll();
        addElement(quadrantList);
        loadStars();

    }

    @Override
    public void ListSelectionChange(String name, int selected) {

    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        scroll = quadrantList.getScroll();
    }
}
