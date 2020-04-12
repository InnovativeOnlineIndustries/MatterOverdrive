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

package matteroverdrive.api.android;

import matteroverdrive.api.renderer.IBioticStatRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

/**
 * Created by Simeon on 7/24/2015.
 * Primary implementation is in {@link matteroverdrive.client.render.AndroidStatRenderRegistry}
 */
@SideOnly(Side.CLIENT)
public interface IAndroidStatRenderRegistry {
    /**
     * Gets a Collection of all registered Render Handlers for a given type of Bionic stat.
     *
     * @param stat The Class (type) of Bionic stat.
     * @return The collection of Render Handlers ({@link matteroverdrive.api.renderer.IBioticStatRenderer}) for the given stat.
     * Returns a Null if there are no registered Render Handlers.
     * @see matteroverdrive.api.renderer.IBioticStatRenderer
     */
    Collection<IBioticStatRenderer> getRendererCollection(Class<? extends IBioticStat> stat);

    /**
     * Removes and returns all renderers assigned to that bionic stat (android ability).
     *
     * @param stat the class/type of bionic stat (android ability).
     * @return a collection of all assigned renderers to that stat class/type.
     */
    Collection<IBioticStatRenderer> removeAllRenderersFor(Class<? extends IBioticStat> stat);

    /**
     * Registers a Render Handler of a given BionicStat.
     *
     * @param stat     The Class of Bionic Stat.
     * @param renderer The Render Handler.
     * @return Did the Render Handler register.
     */
    boolean registerRenderer(Class<? extends IBioticStat> stat, IBioticStatRenderer renderer);
}
