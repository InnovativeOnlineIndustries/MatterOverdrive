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

package matteroverdrive.entity;

import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityFailedSheep extends EntitySheep {
    public EntityFailedSheep(World world, EntitySheep sheep) {
        super(world);
        setFleeceColor(sheep.getFleeceColor());
    }

    public EntityFailedSheep(World world) {
        super(world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MatterOverdriveSounds.failedAnimalIdleSheep;
    }

    protected SoundEvent getHurtSound() {
        return MatterOverdriveSounds.failedAnimalIdleSheep;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MatterOverdriveSounds.failedAnimalDie;
    }

    public EntitySheep createChild(EntityAgeable entity) {
        return new EntityFailedSheep(world, super.createChild(entity));
    }
}
