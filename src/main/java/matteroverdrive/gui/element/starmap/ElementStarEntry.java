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

package matteroverdrive.gui.element.starmap;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.client.render.tileentity.starmap.StarMapRendererStars;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.network.packet.server.starmap.PacketStarMapClientCommands;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class ElementStarEntry extends ElementAbstractStarMapEntry<Star> {

    public ElementStarEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height, Star star) {
        super(gui, groupList, width, height, star);
    }

    @Override
    protected void drawElementName(Star star, Color color, float multiply) {
        String name = spaceBody.getSpaceBodyName();
        GuiStarMap guiStarMap = (GuiStarMap) gui;
        if (guiStarMap.getMachine().getGalaxyPosition().equals(star)) {
            name = "@ " + TextFormatting.ITALIC + name;
        }

        if (Minecraft.getMinecraft().player.capabilities.isCreativeMode || GalaxyClient.getInstance().canSeeStarInfo(star, Minecraft.getMinecraft().player)) {
            RenderUtils.drawString(name, posX + 16, posY + 10, color, multiply);
        } else {
            RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer, name, posX + 16, posY + 10, color, multiply);
        }
    }

    @Override
    protected Map<HoloIcon, Integer> getIcons(Star star) {
        HashMap<HoloIcon, Integer> icons = new HashMap<>();
        HoloIcon homeIcon = ClientProxy.holoIcons.getIcon("home_icon");
        HoloIcon shipIcon = ClientProxy.holoIcons.getIcon("icon_shuttle");
        HoloIcon factoryIcon = ClientProxy.holoIcons.getIcon("factory");
        icons.put(shipIcon, 0);
        icons.put(factoryIcon, 0);
        for (Planet planet : star.getPlanets()) {
            if (planet.isOwner(Minecraft.getMinecraft().player)) {
                if (planet.isHomeworld()) {
                    icons.put(homeIcon, -1);
                }
            }
        }
        return icons;
    }

    @Override
    protected boolean canTravelTo(Star star, EntityPlayer player) {
        return false;
    }

    @Override
    protected boolean canView(Star spaceBody, EntityPlayer player) {
        return true;
    }

    @Override
    protected void onTravelPress() {
        TileEntityMachineStarMap starMap = ((GuiStarMap) gui).getMachine();
        MatterOverdrive.NETWORK.sendToServer(new PacketStarMapClientCommands(starMap, starMap.getZoomLevel(), new GalacticPosition(spaceBody), starMap.getDestination()));
    }

    @Override
    protected void onSelectPress() {
        TileEntityMachineStarMap starMap = ((GuiStarMap) gui).getMachine();
        MatterOverdrive.NETWORK.sendToServer(new PacketStarMapClientCommands(starMap, starMap.getZoomLevel(), starMap.getGalaxyPosition(), new GalacticPosition(spaceBody)));
    }

    protected void onViewPress() {
        gui.setPage(2);
    }

    @Override
    protected Color getSpaceBodyColor(Star star) {
        return StarMapRendererStars.getStarColor(star, Minecraft.getMinecraft().player);
    }

    @Override
    boolean isSelected(Star star) {
        return ((GuiStarMap) gui).getMachine().getDestination().equals(star);
    }

    @Override
    public float getMultiply(Star star) {
        GuiStarMap guiStarMap = (GuiStarMap) gui;
        if (guiStarMap.getMachine().getDestination().equals(star)) {
            return 1;
        } else if (guiStarMap.getMachine().getGalaxyPosition().equals(star)) {
            return 0.5f;
        }
        return 0.1f;
    }
}


