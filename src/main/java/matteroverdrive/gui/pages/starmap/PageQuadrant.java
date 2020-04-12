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

import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.tileentity.starmap.StarMapRendererStars;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.gui.element.starmap.ElementStarEntry;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.client.Minecraft;

public class PageQuadrant extends ElementBaseGroup implements IListHandler {

    private static int scroll;
    private TileEntityMachineStarMap starMap;
    private ElementGroupList starList;

    public PageQuadrant(GuiStarMap gui, int posX, int posY, int width, int height, TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        this.starMap = starMap;
        starList = new ElementGroupList(gui, this, 16, 16, 0, 0);
        starList.setName("Stars");
    }

    private void loadStars() {
        starList.init();
        Quadrant quadrant = GalaxyClient.getInstance().getTheGalaxy().getQuadrant(starMap.getDestination());
        if (quadrant != null) {
            for (Star star : quadrant.getStars()) {
                Color color = StarMapRendererStars.getStarColor(star, Minecraft.getMinecraft().player);
                starList.addElement(new ElementStarEntry((GuiStarMap) gui, starList, 128 + 64, 32, star));

                if (starMap.getDestination().equals(star)) {
                    starList.setSelectedIndex(starList.getElements().size() - 1);
                }
            }
        }
        starList.limitScroll();
    }

    @Override
    public void init() {
        super.init();
        starList.setSize(sizeX, sizeY - 100 - 32);
        starList.setScroll(scroll);
        starList.resetSmoothScroll();
        addElement(starList);
        loadStars();

    }

    @Override
    public void ListSelectionChange(String name, int selected) {

    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        scroll = starList.getScroll();
    }
}
