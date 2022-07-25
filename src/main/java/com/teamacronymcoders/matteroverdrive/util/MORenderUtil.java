package com.teamacronymcoders.matteroverdrive.util;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;

import java.awt.*;

public class MORenderUtil {

    public static int lerp(int a, int b, float lerp) {
        int MASK1 = 0xff00ff;
        int MASK2 = 0x00ff00;

        int f2 = Math.round(256 * lerp);
        int f1 = Math.round(256 - f2);

        return (((((a & MASK1) * f1) + ((b & MASK1) * f2)) >> 8) & MASK1) | (((((a & MASK2) * f1) + ((b & MASK2) * f2)) >> 8) & MASK2);
    }

    public static Color lerp(Color a, Color b, float lerp) {
        return new Color(lerp(a.getRed(), b.getRed(), lerp), lerp(a.getGreen(), b.getGreen(), lerp), lerp(a.getBlue(), b.getBlue(), lerp), lerp(a.getAlpha(), b.getAlpha(), lerp));
    }

    /**
     * Because the vanilla one messes with the GL state too much.
     *
     * @param x0    First X coord
     * @param y0    First Y coord
     * @param x1    Second X coord
     * @param y1    Second Y coord
     * @param color Rectangle color
     */
    public static void fillRect(PoseStack matrixStack, float x0, float y0, float x1, float y1, int color, int alpha)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int r = (color >> 16 & 255);
        int g = (color >> 8 & 255);
        int b = (color & 255);
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder builder = tess.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = matrixStack.last().pose();
        builder.vertex(matrix, x0, y1, 0.0f).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix, x1, y1, 0.0f).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix, x1, y0, 0.0f).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix, x0, y0, 0.0f).color(r, g, b, alpha).endVertex();
        tess.end();
        RenderSystem.enableTexture();
    }

}
