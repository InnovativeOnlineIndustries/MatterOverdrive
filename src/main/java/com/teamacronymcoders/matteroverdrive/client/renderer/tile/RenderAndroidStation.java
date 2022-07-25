/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.teamacronymcoders.matteroverdrive.client.renderer.tile;


import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.teamacronymcoders.matteroverdrive.block.tile.AndroidStationTile;
import com.teamacronymcoders.matteroverdrive.client.MOShaders;
import com.teamacronymcoders.matteroverdrive.reference.ReferenceClient;
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
import net.minecraft.client.renderer.ShaderInstance;
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
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                }, () -> {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                }))
                .setCullState(new RenderStateShard.CullStateShard(false))
                .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                .setOverlayState(new RenderStateShard.OverlayStateShard(true))
                .setShaderState(new RenderStateShard.ShaderStateShard(MOShaders::getAndroidShader)).createCompositeState(false);
        return RenderType.create("android_station", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
    }

    private final PlayerModel<?> playerModel;

    public RenderAndroidStation(BlockEntityRendererProvider.Context rendererContext) {
        super(rendererContext.getBlockEntityRenderDispatcher());
        this.playerModel = new PlayerModel<>(rendererContext.bakeLayer(ModelLayers.PLAYER), false);
        this.playerModel.young = false;
    }

    @Override
    public void drawAdditional(PoseStack stack, MultiBufferSource bufferIn, AndroidStationTile tile, double x, double y, double z, float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && tile.isUsableByPlayer(player)) {
            stack.pushPose();
            stack.translate(0.5,  2,  0.5);
            stack.mulPose(Vector3f.XP.rotationDegrees(180));
            //RenderSystem.depthMask(false);

            RenderSystem.setShaderColor(ReferenceClient.Colors.HOLO.getRed(), ReferenceClient.Colors.HOLO.getGreen(), ReferenceClient.Colors.HOLO.getBlue(), 0.625f);
            float playerPosX = Mth.clampedLerp((float) player.xo, (float) player.position().x, partialTicks);
            float playerPosZ = Mth.clampedLerp((float) player.zo, (float) player.position().z, partialTicks);
            float angle = (float) Math.toDegrees(Math.atan2(playerPosX - (tile.getBlockPos().getX() + 0.5), playerPosZ - (tile.getBlockPos().getZ() + 0.5)) + Math.PI);

            stack.mulPose(Vector3f.YP.rotationDegrees(180));
            stack.mulPose(Vector3f.YN.rotationDegrees(angle));
            /**
             * {@link RenderType#ITEM_ENTITY_TRANSLUCENT_CULL}
             */
            VertexConsumer consumer = bufferIn.getBuffer(ANDROID);

            var modelStack = RenderSystem.getModelViewStack();
            modelStack.pushPose();

            modelStack.mulPoseMatrix(stack.last().pose());

            RenderSystem.applyModelViewMatrix();

            playerModel.renderToBuffer(new PoseStack(), consumer, 0, OverlayTexture.NO_OVERLAY, ReferenceClient.Colors.HOLO.getRed() / 255f, ReferenceClient.Colors.HOLO.getBlue() / 255f, ReferenceClient.Colors.HOLO.getGreen() / 255f, 0.625F);//0.625F);

            // This fixes mojank ;) we don't actually use it. This forces a upload to buffer so the values are not lost.
            bufferIn.getBuffer(RenderType.translucentMovingBlock());
            //RenderSystem.depthMask(true);
            modelStack.popPose();
            RenderSystem.applyModelViewMatrix();
            stack.popPose();
        }
    }

}