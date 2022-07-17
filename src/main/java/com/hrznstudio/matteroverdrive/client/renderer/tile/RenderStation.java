/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.client.renderer.tile;

import com.hrznstudio.matteroverdrive.block.tile.BaseStationTile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Random;

import static com.hrznstudio.matteroverdrive.util.MOColorUtil.HOLO_COLOR;
import static com.hrznstudio.matteroverdrive.util.MOColorUtil.INVALID_HOLO_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE;

public class RenderStation<T extends BaseStationTile> implements BlockEntityRenderer<T> {

    private static final ResourceLocation glowTexture = new ResourceLocation("matteroverdrive:textures/fx/hologram_beam.png");

    private final Random random = new Random();
    public static RenderType TYPE = createRenderType();

    public static RenderType createRenderType() {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(glowTexture, false, false)).setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        })).createCompositeState(true);
        return RenderType.create("render_station", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.TRIANGLE_FAN, 256, false, true, state);
    }

    public RenderStation(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super();
    }

    private void drawHoloLights(PoseStack stack, MultiBufferSource bufferIn, T tile, double x, double y, double z) {
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

        stack.pushPose();
        stack.translate(0,  height, 0);
        VertexConsumer consumer = bufferIn.getBuffer(TYPE);

        Color color;
        if (tile.isUsableByPlayer(Minecraft.getInstance().player)) {
            color = HOLO_COLOR;
        } else {
            color = INVALID_HOLO_COLOR;
        }

        float multiply = 0.5f + (tile.getLevel().getGameTime() % (random.nextInt(70) + 1) == 0 ? (0.05f * random.nextFloat()) : 0.05f);

        Matrix4f matrix = stack.last().pose();

        consumer.vertex(matrix, offset, 0, offset).uv(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,-topSize, height + hologramHeight, -topSize).uv(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,size + topSize, height + hologramHeight, -topSize).uv(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,size, 0, offset).uv(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();

        consumer.vertex(matrix,size, 0, offset).uv(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,size + topSize, height + hologramHeight, -topSize).uv(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,size + topSize, height + hologramHeight, 1 + topSize).uv(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,size, 0, size).uv(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();

        consumer.vertex(matrix,size, 0, size).uv(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,size + topSize, height + hologramHeight, 1 + topSize).uv(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,-topSize, height + hologramHeight, 1 + topSize).uv(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,offset, 0, size).uv(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();

        consumer.vertex(matrix,offset, 0, size).uv(1, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,-topSize, height + hologramHeight, 1 + topSize).uv(1, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,-topSize, height + hologramHeight, -topSize).uv(0, 0).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        consumer.vertex(matrix,offset, 0, offset).uv(0, 1).color(color.getRed() * multiply / 255f, color.getGreen() * multiply / 255f, color.getBlue() * multiply / 255f, 1.0f).endVertex();
        RenderSystem.enableCull();
        RenderSystem.depthMask(false);
        stack.popPose();
    }


    public void drawHoloText(PoseStack stack, T tile, double x, double y, double z, float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (!tile.isUsableByPlayer(player)) {
            stack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL_ONE, GL_ONE);
            stack.translate( 0.5,  0.5,  0.5);
            stack.mulPose(Vector3f.YP.rotationDegrees(180));
            //float playerPosX = (float) MathHelper.clampedLerp((float) player.prevPosX, (float) player.getPosX(), partialTicks);
            //float playerPosZ = (float) MathHelper.clampedLerp((float) player.prevPosZ, (float) player.getPosZ(), partialTicks);
            //float angle = (float) Math.toDegrees(Math.atan2(playerPosX - (tile.getPos().getX() + 0.5), playerPosZ - (tile.getPos().getZ() + 0.5)) + Math.PI);
            stack.mulPose(Vector3f.YP.rotationDegrees(tile.getLevel().getGameTime() % 360));

            RenderSystem.disableCull();

            stack.mulPose(Vector3f.XP.rotationDegrees(180));

            stack.scale(0.02f, 0.02f, 0.02f);
            String[] info = "Access Denied".split(" "); //TODO Translate
            for (int i = 0; i < info.length; i++) {
                int width = Minecraft.getInstance().font.width(info[i]);
                stack.pushPose();
                stack.translate(-width / 2, -32, 0);
                Minecraft.getInstance().font.draw(stack, info[i], 0, i * 10, INVALID_HOLO_COLOR.getRGB());
                stack.popPose();
            }

            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            stack.popPose();
        }
    }

    public void drawAdditional(PoseStack stack, MultiBufferSource bufferIn, T tile, double x, double y, double z, float partialTicks) {

    }

    @Override
    public void render(T tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        drawHoloLights(poseStack, buffer, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        drawHoloText(poseStack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), partialTicks);
        drawAdditional(poseStack, buffer, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), partialTicks);
        poseStack.pushPose();
    }
}