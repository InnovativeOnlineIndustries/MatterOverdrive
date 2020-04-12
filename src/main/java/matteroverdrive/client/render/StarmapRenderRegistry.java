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

package matteroverdrive.client.render;

import matteroverdrive.api.events.MOEventRegisterStarmapRenderer;
import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.starmap.data.SpaceBody;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StarmapRenderRegistry implements IStarmapRenderRegistry {
    final Map<Class<? extends SpaceBody>, Collection<ISpaceBodyHoloRenderer>> map;

    public StarmapRenderRegistry() {
        map = new HashMap<>();
    }

    @Override
    public boolean registerRenderer(Class<? extends SpaceBody> spaceBodyType, ISpaceBodyHoloRenderer renderer) {
        if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterStarmapRenderer(spaceBodyType, renderer))) {
            Collection<ISpaceBodyHoloRenderer> renderers = map.get(spaceBodyType);
            if (renderers == null) {
                renderers = new ArrayList<>();
                map.put(spaceBodyType, renderers);
            }
            return renderers.add(renderer);
        }
        return false;
    }

    @Override
    public Collection<ISpaceBodyHoloRenderer> getStarmapRendererCollection(Class<? extends SpaceBody> spaceBodyType) {
        return map.get(spaceBodyType);
    }
}
