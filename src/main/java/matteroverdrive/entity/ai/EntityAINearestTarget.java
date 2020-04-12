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

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityAINearestTarget<T extends EntityLivingBase> extends EntityAITarget {
    private final Class targetClass;
    private final int targetChance;
    private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    private final Predicate<T> targetEntitySelector;
    private EntityLivingBase targetEntity;

    public EntityAINearestTarget(EntityCreature entity, Class targetClass, int targetChance, boolean shouldCheckSight, boolean nearbyOnly, final Predicate<T> predicate) {
        super(entity, shouldCheckSight, nearbyOnly);
        this.targetClass = targetClass;
        this.targetChance = targetChance;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(entity);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate<T>() {
            /**
             * Return whether the specified entity is applicable to this filter.
             */
            public boolean apply(@Nullable T entity) {
                return !(entity instanceof EntityLivingBase) ? false : (entity != null && !predicate.apply(entity) ? false : EntityAINearestTarget.this.isSuitableTarget(entity, false));
            }
        };
    }

    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            double d0 = this.getTargetDistance();
            List list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
            Collections.sort(list, this.theNearestAttackableTargetSorter);

            if (list.isEmpty()) {
                return false;
            } else {
                this.targetEntity = (EntityLivingBase) list.get(0);
                return true;
            }
        }
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
