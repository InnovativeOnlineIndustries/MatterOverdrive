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

package matteroverdrive.fx;

import matteroverdrive.client.data.TextureAtlasSpriteParticle;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFXGenericAnimatedParticle extends MOEntityFX {
    private boolean bottomPivot;

    public EntityFXGenericAnimatedParticle(World world, double posX, double posY, double posZ, float particleSize, ResourceLocation sprite) {
        super(world, posX, posY, posZ);
        this.particleScale = particleSize;
        TextureAtlasSprite originalSprite = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(sprite);
        if (originalSprite instanceof TextureAtlasSpriteParticle) {
            this.particleTexture = ((TextureAtlasSpriteParticle) originalSprite).copy();
        } else {
            this.particleTexture = originalSprite;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.particleTexture instanceof TextureAtlasSpriteParticle) {
            ((TextureAtlasSpriteParticle) this.particleTexture).updateParticleAnimation();
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (bottomPivot) {
            float f = (float) this.particleTextureIndexX / 16.0F;
            float f1 = f + 0.0624375F;
            float f2 = (float) this.particleTextureIndexY / 16.0F;
            float f3 = f2 + 0.0624375F;
            float f4 = 0.1F * this.particleScale;

            if (this.particleTexture != null) {
                f = this.particleTexture.getMinU();
                f1 = this.particleTexture.getMaxU();
                f2 = this.particleTexture.getMinV();
                f3 = this.particleTexture.getMaxV();
            }

            float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
            float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
            float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
            int i = this.getBrightnessForRender(partialTicks);
            int j = i >> 16 & 65535;
            int k = i & 65535;
            buffer.pos((double) (f5 - rotationX * f4 - rotationXY * f4), (double) (f6), (double) (f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos((double) (f5 - rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4 * 2), (double) (f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos((double) (f5 + rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4 * 2), (double) (f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            buffer.pos((double) (f5 + rotationX * f4 - rotationXY * f4), (double) (f6), (double) (f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        } else {
            super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        }
    }

    public void setBottomPivot(boolean bottomPivot) {
        this.bottomPivot = bottomPivot;
    }
}
