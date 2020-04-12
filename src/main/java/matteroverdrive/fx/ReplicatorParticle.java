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

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ReplicatorParticle extends Particle {
    /**
     * the scale of the flame FX
     */
    private float flameScale;
    private double centerX, centerY, centerZ;
    private double pointGravityScale = 1.0D;

    public ReplicatorParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
        super(world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
        this.motionX = this.motionX * 0.009999999776482582D + xSpeed;
        this.motionY = this.motionY * 0.009999999776482582D + ySpeed;
        this.motionZ = this.motionZ * 0.009999999776482582D + zSpeed;
        double d6 = posX + (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        d6 = posY + (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        d6 = posZ + (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.flameScale = this.particleScale;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        //this.noClip = true;
        this.setParticleTextureIndex(1);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f6 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - f6 * f6 * 0.5F);
        super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public int getBrightnessForRender(float f) {
        float f1 = ((float) this.particleAge + f) / (float) this.particleMaxAge;

        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender(f);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f1 * 15.0F * 16.0F);

        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_) {
        float f1 = ((float) this.particleAge + p_70013_1_) / (float) this.particleMaxAge;

        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        float f2 = super.getBrightnessForRender(p_70013_1_);
        return f2 * f1 + (1.0F - f1);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        //this.moveEntity(this.motionX, this.motionY, this.motionZ);
        Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);
        Vec3d center = new Vec3d(this.centerX, this.centerY, this.centerZ);
        Vec3d position = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d gravityCenter = center.subtract(position);
        gravityCenter = new Vec3d(gravityCenter.x * pointGravityScale, gravityCenter.y * pointGravityScale, gravityCenter.z * pointGravityScale);
        Vec3d dir = gravityCenter.add(motion);

        this.motionX = dir.x;
        this.motionY = dir.y;
        this.motionZ = dir.z;

        this.getBoundingBox().offset(this.motionX, this.motionY, this.motionZ);
        this.posX = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0D;
        this.posY = this.getBoundingBox().minY - (double) this.height;
        this.posZ = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0D;

        double speedOverTime = 1D;
        this.motionX *= speedOverTime;
        this.motionY *= speedOverTime;
        this.motionZ *= speedOverTime;
        move(this.motionZ, this.motionY, this.motionZ);
    }

    public void setCenter(double x, double y, double z) {
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
    }

    public void setParticleAge(int age) {
        this.particleMaxAge = age;
    }

    public double getPointGravityScale() {
        return pointGravityScale;
    }

    public void setPointGravityScale(double pointGravityScale) {
        this.pointGravityScale = pointGravityScale;
    }
}
