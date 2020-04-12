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

import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.client.Minecraft;

public class StarmapHelper {
    public static void drawPlanetInfo(Planet planet, String text, int x, int y) {
        drawPlanetInfo(planet, text, x, y, 1);
    }

    public static void drawPlanetInfo(Planet planet, String text, int x, int y, float multiply) {
        drawPlanetInfo(planet, text, x, y, 1, false);
    }

    public static void drawPlanetInfo(Planet planet, String text, int x, int y, float multiply, boolean unicode) {
        boolean lastUnicode;
        if (GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().player) || Minecraft.getMinecraft().player.capabilities.isCreativeMode) {
            lastUnicode = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(unicode);
            Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, Planet.getGuiColor(planet).multiplyWithoutAlpha(multiply).getColor());
            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(lastUnicode);
        } else {
            lastUnicode = Minecraft.getMinecraft().standardGalacticFontRenderer.getUnicodeFlag();
            Minecraft.getMinecraft().standardGalacticFontRenderer.setUnicodeFlag(unicode);
            Minecraft.getMinecraft().standardGalacticFontRenderer.drawString(text, x, y, Planet.getGuiColor(planet).multiplyWithoutAlpha(multiply).getColor());
            Minecraft.getMinecraft().standardGalacticFontRenderer.setUnicodeFlag(lastUnicode);
        }
    }
}
