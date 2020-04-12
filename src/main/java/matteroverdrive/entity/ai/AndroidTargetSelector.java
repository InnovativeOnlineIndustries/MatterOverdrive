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
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class AndroidTargetSelector implements Predicate<Entity> {
    private final EntityRougeAndroidMob mob;

    public AndroidTargetSelector(EntityRougeAndroidMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean apply(@Nullable Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (mob.hasTeam()) {
                return entity.getTeam() != null && !entity.getTeam().isSameTeam(mob.getTeam());
            } else {
                AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(entity);
                if (androidPlayer == null || !androidPlayer.isAndroid()) {
                    return true;
                }
            }
        } else if (entity instanceof EntityMutantScientist) {
            return true;
        } else if (entity instanceof EntityRougeAndroidMob) {
            if (mob.hasTeam() && ((EntityRougeAndroidMob) entity).hasTeam()) {
                return !((EntityRougeAndroidMob) entity).getTeam().isSameTeam(mob.getTeam());
            }
        }
        return false;
    }
}
