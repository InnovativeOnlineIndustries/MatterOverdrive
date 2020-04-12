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

import matteroverdrive.client.data.Color;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ElementParallaxBackground extends MOElementBase implements IParallaxElement {
    private float texU;
    private float texV;
    private boolean strech;
    private float parallaxMultiply;
    private Color color;
    private int textureWidth;

    public ElementParallaxBackground(MOGuiBase gui, int posX, int posY, int width, int height, boolean strech, float parallaxMultiply) {
        super(gui, posX, posY, width, height);
        this.strech = strech;
        this.parallaxMultiply = parallaxMultiply;
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
    public void drawBackground(int var1, int var2, float var3) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha();
        RenderUtils.applyColorWithAlpha(color);
        RenderUtils.bindTexture(texture);

        if (strech) {
            if (color != null) {
                RenderUtils.drawPlaneWithUV(posX, posY, 0, getWidth(), getHeight(), texU, texV, 1, 1);
            } else {
                RenderUtils.drawPlaneWithUV(posX, posY, 0, getWidth(), getHeight(), texU, texV, 1, 1);
            }
        } else {
            if (color != null) {
                RenderUtils.drawPlaneWithUV(posX, posY, 0, getWidth(), getHeight(), texU, texV, (float) getWidth() / (float) texW, (float) getHeight() / (float) texH);
            } else {
                RenderUtils.drawPlaneWithUV(posX, posY, 0, getWidth(), getHeight(), texU, texV, (float) getWidth() / (float) texW, (float) getHeight() / (float) texH);
            }
        }

        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawForeground(int var1, int var2) {

    }

    @Override
    public void move(int deltaX, int deltaY) {
        texU -= deltaX * parallaxMultiply;
        texV -= deltaY * parallaxMultiply;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
