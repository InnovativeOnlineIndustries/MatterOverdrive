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

package matteroverdrive.util;

import net.minecraft.world.World;

public class TimeTracker {
    private long lastMark = -9223372036854775808L;

    public TimeTracker() {
    }

    public boolean hasDelayPassed(World world, int time) {
        long worldTime = world.getTotalWorldTime();
        if (worldTime < this.lastMark) {
            this.lastMark = worldTime;
            return false;
        } else if (this.lastMark + (long) time <= worldTime) {
            this.lastMark = worldTime;
            return true;
        } else {
            return false;
        }
    }

    public void markTime(World var1) {
        this.lastMark = var1.getTotalWorldTime();
    }
}
