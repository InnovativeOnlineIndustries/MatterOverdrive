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

package matteroverdrive.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class WeaponMetadataSection implements IMetadataSection {
    private final Map<String, Vec3d> modulePositions;

    public WeaponMetadataSection() {
        this.modulePositions = new HashMap<>();
    }

    public Map<String, Vec3d> getModulePositions() {
        return modulePositions;
    }

    public void setModulePosition(String module, Vec3d pos) {
        modulePositions.put(module, pos);
    }

    public Vec3d getModulePosition(String module, Vec3d def) {
        Vec3d moduelPos = modulePositions.get(module);
        if (moduelPos != null) {
            return moduelPos;
        } else {
            return def;
        }
    }

    public Vec3d getModulePosition(String module) {
        return modulePositions.get(module);
    }
}
