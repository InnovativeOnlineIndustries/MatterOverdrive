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

package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public abstract class StarMapRendererStars extends StarMapRendererAbstract {
    public static Color getStarColor(Star star, EntityPlayer player) {
        return new Color(star.getColor());
    }

    protected void renderStars(Quadrant quadrant, TileEntityMachineStarMap starMap, double distanceMultiply, double starSizeMultiply) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        GlStateManager.color(1, 1, 1, 1);
        Vec3d pos;

        if (quadrant != null) {
            glLineWidth(1);

            Star from = null, to = null;
            ClientProxy.renderHandler.getRenderParticlesHandler().bindSheet();
            for (Star star : quadrant.getStars()) {
                pos = new Vec3d(star.getPosition().x * distanceMultiply, star.getPosition().y * distanceMultiply, star.getPosition().z * distanceMultiply);
                drawStarParticle(quadrant, star, pos, player, starMap, starSizeMultiply);
                if (starMap.getGalaxyPosition().equals(star)) {
                    from = star;
                }
                if (starMap.getDestination().equals(star)) {
                    to = star;
                }
            }

            if (from != null && to != null && from != to) {
                drawConnection(from, to, distanceMultiply);
            }
            //Tessellator.getInstance().draw();
        }
    }

    protected void drawConnection(Star from, Star to, double distanceMultiply) {
        GlStateManager.disableTexture2D();
        RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.3f);
        glBegin(GL_LINE_STRIP);
        glVertex3d(from.getX() * distanceMultiply, from.getY() * distanceMultiply, from.getZ() * distanceMultiply);
        glVertex3d(to.getX() * distanceMultiply, to.getY() * distanceMultiply, to.getZ() * distanceMultiply);
        glEnd();
        GlStateManager.enableTexture2D();
    }

    protected void drawStarParticle(Quadrant quadrant, Star star, Vec3d pos, EntityPlayer player, TileEntityMachineStarMap starMap, double starSizeMultiply) {
        Color color = getStarColor(star, player);
        double size = 0.01;
        if (starMap.getDestination().equals(star)) {
            size = 0.035;
            RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), selectedIcon, star.getSize() * 0.05 * starSizeMultiply, pos, color);
        }
        if (starMap.getGalaxyPosition().equals(star)) {
            size = 0.035;
            RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), currentIcon, star.getSize() * 0.05 * starSizeMultiply, pos, color);
        }
        if (star.isClaimed(player) == 3) {
            size = 0.025;
        }
        RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), star_icon, star.getSize() * size * starSizeMultiply, pos, color);
    }
}
