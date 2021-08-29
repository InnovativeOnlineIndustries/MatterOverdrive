/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.client.renderer.tile;


import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.block.tile.AndroidStationTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderAndroidStation extends RenderStation<AndroidStationTile> {

    private static final ResourceLocation location = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/entity/android_colorless.png");
    public static RenderType ANDROID = createRenderAndroidType();
    public static RenderType createRenderAndroidType() {
        RenderType.State state = RenderType.State.getBuilder().texture(new RenderState.TextureState(location, false, false)).transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                }, () -> {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                }))
                .alpha(new RenderState.AlphaState(0.003921569F))
                .build(true);
        return RenderType.makeType("android_station", DefaultVertexFormats.ENTITY, 7, 256, true, true, state);
    }

    public static PlayerModel playerModel = new PlayerModel(0.4f, false);

    public RenderAndroidStation(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        playerModel.isChild = false;
    }



    @Override
    public void drawAdditional(MatrixStack stack, IRenderTypeBuffer bufferIn, AndroidStationTile tile, double x, double y, double z, float partialTicks) {
        if (tile.isUsableByPlayer(Minecraft.getInstance().player)) {
            stack.push();
            stack.translate(0.5,  2,  0.5);
            stack.rotate(Vector3f.XP.rotationDegrees(180));
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);


            //GlStateManager.color(ReferenceClient.Colors.HOLO.getRed(), ReferenceClient.Colors.HOLO.getGreen(), ReferenceClient.Colors.HOLO.getBlue());
            //float playerPosX = (float) MathHelper.clampedLerp((float) player.prevPosX, (float) player.getPosX(), partialTicks);
            //float playerPosZ = (float) MathHelper.clampedLerp((float) player.prevPosZ, (float) player.getPosZ(), partialTicks);
            //float angle = (float) Math.toDegrees(Math.atan2(playerPosX - (tile.getPos().getX() + 0.5), playerPosZ - (tile.getPos().getZ() + 0.5)) + Math.PI);
            stack.rotate(Vector3f.YP.rotationDegrees(180));
            stack.rotate(Vector3f.YN.rotationDegrees(tile.getWorld().getGameTime() % 360));
            IVertexBuilder buffer = bufferIn.getBuffer(ANDROID);
            float multiply = 0.35f + (tile.getWorld().getGameTime() % (tile.getWorld().rand.nextInt(70) + 1) == 0 ? (0.05f * tile.getWorld().rand.nextFloat()) : 0.05f);
            playerModel.render(stack, buffer ,0, 0, multiply, multiply, multiply, 0.0625F);

            RenderSystem.enableCull();
            RenderSystem.depthMask(false);
            stack.pop();
        }
    }

}