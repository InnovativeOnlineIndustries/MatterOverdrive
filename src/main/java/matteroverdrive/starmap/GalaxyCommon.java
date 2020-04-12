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

import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public abstract class GalaxyCommon {
    protected final Random random;
    protected final HashMap<UUID, Planet> homePlanets;
    protected Galaxy theGalaxy;
    protected World world;

    public GalaxyCommon() {
        random = new Random();
        homePlanets = new HashMap<>();
    }

    public void loadClaimedPlanets() {
        homePlanets.clear();

        for (Quadrant quadrant : theGalaxy.getQuadrants()) {
            for (Star star : quadrant.getStars()) {
                for (Planet planet : star.getPlanets()) {
                    if (planet.isHomeworld() && planet.hasOwner()) {
                        homePlanets.put(planet.getOwnerUUID(), planet);
                    }
                }
            }
        }
    }

    //region getters and setters
    public Planet getPlanet(GalacticPosition position) {
        if (theGalaxy != null) {
            return theGalaxy.getPlanet(position);
        }
        return null;
    }

    public Star getStar(GalacticPosition position) {
        if (theGalaxy != null) {
            return theGalaxy.getStar(position);
        }
        return null;
    }

    public Quadrant getQuadrant(GalacticPosition position) {
        if (theGalaxy != null) {
            return theGalaxy.getQuadrant(position);
        }
        return null;
    }

    public Planet getHomeworld(EntityPlayer player) {
        return homePlanets.get(EntityPlayer.getUUID(player.getGameProfile()));
    }

    public Galaxy getTheGalaxy() {
        return theGalaxy;
    }

    public void setTheGalaxy(Galaxy galaxy) {
        theGalaxy = galaxy;
        if (theGalaxy != null) {
            loadClaimedPlanets();
        }
    }
    //endregion
}
