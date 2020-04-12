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

package matteroverdrive.entity.ai;

import matteroverdrive.api.entity.IRangedEnergyWeaponAttackMob;
import matteroverdrive.items.weapon.EnergyWeapon;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.Vec3d;

public class EntityAIPhaserBoltAttack extends EntityAIBase {
    /**
     * The entity the AI instance has been applied to
     */
    private final EntityLiving entityHost;
    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final IRangedEnergyWeaponAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private Vec3d lastKnownShootLocation;
    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackDelay.
     */
    private int rangedAttackDelayTime;
    private double entityMoveSpeed;
    private int pathRetryTimer;
    private int shootPatienceTime;
    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    private int maxRangedAttackDelay;
    private float maxChaseDistance;
    private float maxChaseDistanceSq;
    private Path lastChasePath;

    public EntityAIPhaserBoltAttack(IRangedEnergyWeaponAttackMob rangedAttackEntityHost, double entityMoveSpeed, int maxRangedAttackDelay, float maxChaseDistance) {
        this.rangedAttackDelayTime = -1;

        if (!(rangedAttackEntityHost instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("EntityAIPhaserBoltAttack requires Mob implements IRangedEnergyWeaponAttackMob");
        } else {
            this.rangedAttackEntityHost = rangedAttackEntityHost;
            this.entityHost = (EntityLiving) rangedAttackEntityHost;
            this.entityMoveSpeed = entityMoveSpeed;
            this.maxRangedAttackDelay = maxRangedAttackDelay;
            this.maxChaseDistance = maxChaseDistance;
            this.maxChaseDistanceSq = maxChaseDistance * maxChaseDistance;
            this.setMutexBits(3);
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();

        if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isDead) {
            this.attackTarget = entitylivingbase;
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.attackTarget = null;
        this.pathRetryTimer = 0;
        this.rangedAttackDelayTime = -1;
        this.shootPatienceTime = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        double distanceToTargetSq = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean canSee = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (canSee) {
            lastKnownShootLocation = new Vec3d(attackTarget.prevPosX, attackTarget.prevPosY, attackTarget.prevPosZ);
            shootPatienceTime = 60;
            ++this.pathRetryTimer;
        } else {
            this.pathRetryTimer = 0;
        }

        manageMovingToLastKnowMoveLocation(distanceToTargetSq);

        if (shootPatienceTime == 0) {
            lastKnownShootLocation = null;
        }
        if (lastKnownShootLocation != null) {
            manageShooting(canSee, distanceToTargetSq);
            shootPatienceTime--;
        } else {
            this.entityHost.getLookHelper().setLookPosition(attackTarget.posX, attackTarget.posY + attackTarget.getEyeHeight(), attackTarget.posZ, 30.0F, 30.0F);
        }
    }

    private void manageMovingToLastKnowMoveLocation(double distanceToTargetSq) {
        if (distanceToTargetSq <= (double) this.maxChaseDistanceSq && this.pathRetryTimer >= 20) {
            if (this.entityHost.getNavigator().getPath() != null && this.entityHost.getNavigator().getPath().equals(lastChasePath)) {
                this.entityHost.getNavigator().clearPath();
            }
        } else if (this.entityHost.getNavigator().noPath()) {
            lastChasePath = this.entityHost.getNavigator().getPathToEntityLiving(attackTarget);
            this.entityHost.getNavigator().setPath(lastChasePath, this.entityMoveSpeed);
        }
    }

    private void manageShooting(boolean canSeeTarget, double distanceToTargetSq) {
        this.entityHost.getLookHelper().setLookPosition(this.lastKnownShootLocation.x, this.lastKnownShootLocation.y + attackTarget.getEyeHeight(), this.lastKnownShootLocation.z, 30.0F, 30.0F);
        ItemStack weapon = rangedAttackEntityHost.getWeapon();

        if (this.rangedAttackDelayTime == 0 && weapon != null && weapon.getItem() instanceof EnergyWeapon && ((EnergyWeapon) weapon.getItem()).canFire(weapon, entityHost.world, entityHost)) {
            if (distanceToTargetSq > (double) this.maxChaseDistanceSq) {
                return;
            }

            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lastKnownShootLocation, canSeeTarget);
            this.rangedAttackDelayTime = this.maxRangedAttackDelay;
        } else if (this.rangedAttackDelayTime < 0) {
            this.rangedAttackDelayTime = this.maxRangedAttackDelay;
        } else if (this.rangedAttackDelayTime > 0) {
            this.rangedAttackDelayTime--;
        }
    }

    public void setMaxRangedAttackDelay(int time) {
        this.maxRangedAttackDelay = time;
    }

    public void setMaxChaseDistance(int maxChaseDistance) {
        this.maxChaseDistance = maxChaseDistance;
        this.maxChaseDistanceSq = maxChaseDistance * maxChaseDistance;
    }
}
