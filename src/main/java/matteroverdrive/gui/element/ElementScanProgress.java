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

package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class ElementScanProgress extends MOElementBase {
    private static float NoiseSize = 0.1f;
    Random random = new Random();
    int seed = 0;
    float progress;
    float[] values;

    public ElementScanProgress(MOGuiBase gui, int posX, int posY) {
        super(gui, posX, posY);
        this.setTexture(Reference.PATH_ELEMENTS + "screen.png", 117, 47);
        values = new float[26];
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void init() {

    }

    @Override
    public void addTooltip(List<String> var1, int mouseX, int mouseY) {

    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 0);

        gui.bindTexture(texture);
        this.drawTexturedModalRect(0, 0, 0, 0, 117, 47);
        Color color = new Color(191, 228, 230);

        random.setSeed(seed);
        int progress = MathHelper.floor(this.progress * 26);

        int marginsTop = 8;
        int marginsLeft = 7;
        int maxHeight = 32;

        for (int i = 0; i < 26; i++) {
            float newValue;

            if (i < progress) {
                double noiseValue = ((MOMathHelper.noise(0, 0.05f * i, Math.pow(seed * 100, 2)) + 1.0) / 2.0);
                double contrastFactor = 2;
                noiseValue = contrastFactor * (noiseValue - 0.5) + 0.5;
                noiseValue = Math.pow(Math.min(noiseValue, 1), 2);
                noiseValue = noiseValue * 0.8 + random.nextDouble() * 0.2;

                newValue = (float) noiseValue;
                int height = Math.round(values[i] * maxHeight);
                int x1 = marginsLeft + i * 4;
                int y1 = maxHeight + marginsTop;
                int x2 = x1 + 2;
                int y2 = maxHeight - height + marginsTop;

                gui.drawSizedRect(x1, y1, x2, y2, color.getColor());
                gui.drawSizedRect(x1, y2 - 1, x2, y2 - 2, color.getColor());
            } else {
                newValue = 0;
            }

            values[i] = MOMathHelper.Lerp(values[i], newValue, 0.05f);
        }

        GlStateManager.popMatrix();
        GlStateManager.color(1, 1, 1);
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
