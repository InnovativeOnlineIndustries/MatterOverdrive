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

package matteroverdrive.starmap;

import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GalaxyClient extends GalaxyCommon {
    //region Private Vars
    private static GalaxyClient instance;
    //endregion

    //region Constructors
    public GalaxyClient() {
        super();
    }
    //endregion

    //region Getters and Setters
    public static GalaxyClient getInstance() {
        if (instance == null) {
            instance = new GalaxyClient();
        }

        return instance;
    }

    public boolean canSeePlanetInfo(Planet planet, EntityPlayer player) {
        if (planet.isOwner(player) || player.capabilities.isCreativeMode) {
            return true;
        }

        return false;
    }

    public boolean canSeeStarInfo(Star star, EntityPlayer player) {
        for (Planet planet : star.getPlanets()) {
            if (canSeePlanetInfo(planet, player)) {
                return true;
            }
        }
        return false;
    }
    //endregion

    //region Events
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world != null &&
                theGalaxy != null &&
                !Minecraft.getMinecraft().isGamePaused() &&
                Minecraft.getMinecraft().world.isRemote &&
                Minecraft.getMinecraft().world.provider.getDimension() == 0 &&
                event.phase == TickEvent.Phase.START &&
                Minecraft.getMinecraft().world != null) {
            theGalaxy.update(Minecraft.getMinecraft().world);
        }
    }
    //endregion
}
