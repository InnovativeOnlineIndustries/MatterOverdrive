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

package matteroverdrive.handler;

import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;

public class MatterNetworkTickHandler implements IConfigSubscriber {
    int id_count;
    int last_ID;
    private int max_broadcasts;
    private int broadcastCount;

    public void updateHandler(IMatterNetworkHandler handler, TickEvent.Phase phase, World world) {

        if (broadcastCount < max_broadcasts) {
            if (id_count >= last_ID) {
                try {
                    broadcastCount += handler.onNetworkTick(world, phase);
                } catch (Exception e) {
                    MOLog.log(Level.FATAL, e, "There was a problem while ticking MatterNetworkHandler %s", handler);
                }
            }

            id_count++;
        }
    }

    public void onWorldTickPre(TickEvent.Phase phase, World world) {
        //reset the broadcast counting each tick
        broadcastCount = 0;
        id_count = 0;
    }

    public void onWorldTickPost(TickEvent.Phase phase, World world) {
        if (broadcastCount >= max_broadcasts) {
            //if the broadcast count exceeded the maximum then store the last ID from the ID count.
            //this will restart the broadcasting next tick from the last broadcaster not the beginning.
            last_ID = id_count;
        } else {
            //resets the last ID if broadcast did not exceed the maximum.
            //this will start the broadcasting from beginning next tick.
            last_ID = 0;
        }
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        this.max_broadcasts = config.getInt(ConfigurationHandler.KEY_MAX_BROADCASTS, ConfigurationHandler.CATEGORY_MATTER_NETWORK, 128, "The maximum amount of network packet broadcasts per tick.");
    }
}
