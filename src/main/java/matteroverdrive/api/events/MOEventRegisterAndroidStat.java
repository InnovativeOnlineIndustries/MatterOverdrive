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

package matteroverdrive.api.events;

import matteroverdrive.api.android.IBioticStat;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 7/21/2015.
 * Triggered when an Android Ability is registered.
 * When canceled, the ability will not be registered.
 */
public class MOEventRegisterAndroidStat extends Event {
    /**
     * The Bionic Stat to be registered
     */
    public final IBioticStat stat;

    public MOEventRegisterAndroidStat(IBioticStat stat) {
        this.stat = stat;
    }

    public boolean isCancelable() {
        return true;
    }
}
