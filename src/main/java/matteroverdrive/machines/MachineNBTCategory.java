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

package matteroverdrive.machines;

import java.util.EnumSet;

public enum MachineNBTCategory {
    DATA,
    CONFIGS,
    INVENTORY,
    GUI;

    public static final EnumSet<MachineNBTCategory> ALL_OPTS = EnumSet.allOf(MachineNBTCategory.class);

    public static int encode(EnumSet<MachineNBTCategory> set) {
        int ret = 0;

        for (MachineNBTCategory val : set) {
            ret |= 1 << val.ordinal();
        }

        return ret;
    }

    public static EnumSet<MachineNBTCategory> decode(int code) {
        MachineNBTCategory[] values = MachineNBTCategory.values();
        EnumSet<MachineNBTCategory> result = EnumSet.noneOf(MachineNBTCategory.class);
        while (code != 0) {
            int ordinal = Integer.numberOfTrailingZeros(code);
            code ^= Integer.lowestOneBit(code);
            result.add(values[ordinal]);
        }
        return result;
    }
}
