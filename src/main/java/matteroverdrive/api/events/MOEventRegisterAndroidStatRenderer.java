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
import matteroverdrive.api.renderer.IBioticStatRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 7/24/2015.
 * Triggered by special Bionic Stats that have custom renderers, such as the Shield ability.
 */
public class MOEventRegisterAndroidStatRenderer extends Event {
    /**
     * The type of bionic stat that is being rendered.
     */
    public final Class<? extends IBioticStat> statClass;
    /**
     * The Bionic Stat renderer itself.
     */
    public final IBioticStatRenderer renderer;

    public MOEventRegisterAndroidStatRenderer(Class<? extends IBioticStat> statClass, IBioticStatRenderer renderer) {
        this.statClass = statClass;
        this.renderer = renderer;
    }

    public boolean isCancelable() {
        return true;
    }
}
