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

package matteroverdrive.gui.android;

public enum AndroidHudPosition {
    TOP_LEFT(0, 0, "Top Left"),
    TOP_CENTER(0.5f, 0, "Top Center"),
    TOP_RIGHT(1, 0, "Top Right"),
    MIDDLE_LEFT(0, 0.5f, "Middle Left"),
    MIDDLE_CENTER(0.5f, 0.5f, "Middle Center"),
    MIDDLE_RIGHT(1f, 0.5f, "Middle Right"),
    BOTTOM_LEFT(0, 1, "Bottom Left"),
    BOTTOM_CENTER(0.5f, 1, "Bottom Center"),
    BOTTOM_RIGHT(1, 1, "Bottom Right");

    public final float x;
    public final float y;
    private final String name;

    AndroidHudPosition(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public static String[] getNames() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].name;
        }
        return names;
    }
}
