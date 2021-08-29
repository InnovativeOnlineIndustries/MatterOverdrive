/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.client.renderer.tile;

import com.hrznstudio.matteroverdrive.block.tile.BaseStationTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import java.awt.*;
import java.util.Random;

import static com.hrznstudio.matteroverdrive.util.MOColorUtil.HOLO_COLOR;
import static com.hrznstudio.matteroverdrive.util.MOColorUtil.INVALID_HOLO_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE;

public class RenderStation<T extends BaseStationTile> extends TileEntityRenderer<T> {


    private static final ResourceLocation glowTexture = new ResourceLocation("matteroverdrive:textures/fx/hologram_beam.png");

    private final Random random = new Random();
    public static RenderType TYPE = createRenderType();

    public static RenderType createRenderType() {
        RenderType.State state = RenderType.State.getBuilder().texture(new RenderState.TextureState(glowTexture, false, false)).transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        }, () -> {
            RenderSystem.disableBlend();
        })).build(true);
        return RenderType.makeType("render_station", DefaultVertexFormats.POSITION_TEX_COLOR, 7, 256, false, true, state);
    }

    public RenderStation(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    private void drawHoloLights(MatrixStack stack, IRenderTypeBuffer bufferIn, T tile, double x, double y, double z) {
        //float lastLightMapX = OpenGlHelper.lastBrightnessX;
        //float lastLightMapY = OpenGlHelper.lastBrightnessY;
        //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);


        float height = 5.5f * (1f / 16f);
        float hologramHeight = 1;
        float offset = 2f * (1f / 16f);
        float size = 14f * (1f / 16f);
        float topSize = 2 - 1;

        stack.push();
        stack.translate(0,  height, 0);
        IVertexBuilder buffer = bufferIn.getBuffer(TYPE);

        Color color;
        if (tile.isUsableByPlayer(Minecraft.getInstance().player)) {
            color = HOLO_COLOR;
        } else {
            color = INVALID_HOLO_COLOR;
        }

        float multiply = 0.5f + (tile.getWorld().getGameTime() % (random.nextInt(70) + 1) == 0 ? (0.05f * random.nextFloat()) : 0.05f);

        Matrix4f matrix = stack.getLast().getMatrix();

        buffer.pos(matrix, offset, 0, offset).tex(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,-topSize, height + hologramHeight, -topSize).tex(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,size + topSize, height + hologramHeight, -topSize).tex(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,size, 0, offset).tex(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();

        buffer.pos(matrix,size, 0, offset).tex(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,size + topSize, height + hologramHeight, -topSize).tex(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,size + topSize, height + hologramHeight, 1 + topSize).tex(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,size, 0, size).tex(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();

        buffer.pos(matrix,size, 0, size).tex(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,size + topSize, height + hologramHeight, 1 + topSize).tex(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,-topSize, height + hologramHeight, 1 + topSize).tex(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,offset, 0, size).tex(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();

        buffer.pos(matrix,offset, 0, size).tex(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,-topSize, height + hologramHeight, 1 + topSize).tex(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,-topSize, height + hologramHeight, -topSize).tex(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        buffer.pos(matrix,offset, 0, offset).tex(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        RenderSystem.enableCull();
        RenderSystem.depthMask(false);
        stack.pop();
    }


    public void drawHoloText(MatrixStack stack, T tile, double x, double y, double z, float partialTicks) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (!tile.isUsableByPlayer(player)) {
            stack.push();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL_ONE, GL_ONE);
            stack.translate( 0.5,  0.5,  0.5);
            stack.rotate(Vector3f.YP.rotationDegrees(180));
            //float playerPosX = (float) MathHelper.clampedLerp((float) player.prevPosX, (float) player.getPosX(), partialTicks);
            //float playerPosZ = (float) MathHelper.clampedLerp((float) player.prevPosZ, (float) player.getPosZ(), partialTicks);
            //float angle = (float) Math.toDegrees(Math.atan2(playerPosX - (tile.getPos().getX() + 0.5), playerPosZ - (tile.getPos().getZ() + 0.5)) + Math.PI);
            stack.rotate(Vector3f.YP.rotationDegrees(tile.getWorld().getGameTime() % 360));

            RenderSystem.disableCull();
            RenderSystem.disableLighting();

            stack.rotate(Vector3f.XP.rotationDegrees(180));

            stack.scale(0.02f, 0.02f, 0.02f);
            String info[] = "Access Denied".split(" "); //TODO Translate
            for (int i = 0; i < info.length; i++) {
                int width = Minecraft.getInstance().fontRenderer.getStringWidth(info[i]);
                stack.push();
                stack.translate(-width / 2, -32, 0);
                Minecraft.getInstance().fontRenderer.drawString(stack, info[i], 0, i * 10, INVALID_HOLO_COLOR.getRGB());
                stack.pop();
            }

            RenderSystem.enableLighting();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            stack.pop();
        }
    }

    public void drawAdditional(MatrixStack stack, IRenderTypeBuffer bufferIn, T tile, double x, double y, double z, float partialTicks) {
        
    }

    @Override
    public void render(T tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        drawHoloLights(matrixStackIn, bufferIn, tile, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
        drawHoloText(matrixStackIn, tile, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), partialTicks);
        drawAdditional(matrixStackIn, bufferIn, tile, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), partialTicks);
        matrixStackIn.pop();
    }
}