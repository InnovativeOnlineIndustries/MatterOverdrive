/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.client.renderer.tile;


import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.block.tile.AndroidStationTile;
import com.hrznstudio.matteroverdrive.client.MOShaders;
import com.hrznstudio.matteroverdrive.reference.ReferenceClient;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderAndroidStation extends RenderStation<AndroidStationTile> {

    private static final ResourceLocation location = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/entity/android_colorless.png");
    public static RenderType ANDROID = createRenderAndroidType();
    public static RenderType createRenderAndroidType() {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(location, false, false)).setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                }, () -> {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                }))
                .setShaderState(new RenderStateShard.ShaderStateShard(MOShaders::getAndroidShader)).createCompositeState(true);
        return RenderType.create("android_station", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
    }

    public PlayerModel playerModel;

    public RenderAndroidStation(BlockEntityRendererProvider.Context rendererContext) {
        super(rendererContext.getBlockEntityRenderDispatcher());
        playerModel = new PlayerModel(rendererContext.bakeLayer(ModelLayers.PLAYER), false);
        playerModel.young = false;
    }

    @Override
    public void drawAdditional(PoseStack stack, MultiBufferSource bufferIn, AndroidStationTile tile, double x, double y, double z, float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && tile.isUsableByPlayer(player)) {
            stack.pushPose();
            stack.translate(0.5,  2,  0.5);
            stack.mulPose(Vector3f.XP.rotationDegrees(180));
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);

            RenderSystem.setShaderColor(ReferenceClient.Colors.HOLO.getRed(), ReferenceClient.Colors.HOLO.getGreen(), ReferenceClient.Colors.HOLO.getBlue(), 1.0f);
            float playerPosX = (float) Mth.clampedLerp((float) player.xo, (float) player.position().x, partialTicks);
            float playerPosZ = (float) Mth.clampedLerp((float) player.zo, (float) player.position().z, partialTicks);
            float angle = (float) Math.toDegrees(Math.atan2(playerPosX - (tile.getBlockPos().getX() + 0.5), playerPosZ - (tile.getBlockPos().getZ() + 0.5)) + Math.PI);

            stack.mulPose(Vector3f.YP.rotationDegrees(180));
            stack.mulPose(Vector3f.YN.rotationDegrees(angle));
            VertexConsumer consumer = bufferIn.getBuffer(ANDROID);
            float multiply = 0.35f + (tile.getLevel().getGameTime() % (tile.getLevel().random.nextInt(70) + 1) == 0 ? (0.05f * tile.getLevel().random.nextFloat()) : 0.05f);
            playerModel.renderToBuffer(stack, consumer, 15728640, OverlayTexture.NO_OVERLAY, ReferenceClient.Colors.HOLO.getRed(), ReferenceClient.Colors.HOLO.getBlue(), ReferenceClient.Colors.HOLO.getGreen(), 0.625F);

            RenderSystem.enableCull();
            RenderSystem.depthMask(false);
            stack.popPose();
        }
    }

}