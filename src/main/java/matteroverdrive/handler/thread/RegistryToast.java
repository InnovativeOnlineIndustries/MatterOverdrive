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

package matteroverdrive.handler.thread;

import matteroverdrive.MatterOverdrive;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RegistryToast implements IToast {
    boolean begin;
    long time;
    private long firstDrawTime = 0;

    public RegistryToast(boolean begin, long time) {
        this.begin = begin;
        this.time = time;
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta) {
        if (firstDrawTime == 0)
            this.firstDrawTime = delta;
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        toastGui.getMinecraft().fontRenderer.drawString("Recipe Calculation", 5, 7, -11534256);
        if (begin) {
            toastGui.getMinecraft().fontRenderer.drawString("Starting calculation", 5, 18, -16777216);
        } else {
            toastGui.getMinecraft().fontRenderer.drawString("Calculation complete", 5, 18, -16777216);
        }
        RenderHelper.enableGUIStandardItemLighting();
        if (begin && MatterOverdrive.MATTER_REGISTRY.hasComplitedRegistration)
            return Visibility.HIDE;
        return delta - this.firstDrawTime >= time ? Visibility.HIDE : Visibility.SHOW;
    }
}
