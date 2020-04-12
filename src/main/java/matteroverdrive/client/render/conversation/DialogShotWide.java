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

package matteroverdrive.client.render.conversation;

import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class DialogShotWide extends DialogShot {
    private final float distance;
    private final float heightOffset;
    private final boolean oppositeSide;

    public DialogShotWide(float heightOffset, boolean oppositeSide, float distance) {
        this.heightOffset = heightOffset;
        this.oppositeSide = oppositeSide;
        this.distance = distance;
    }

    @Override
    public boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks, EntityRendererConversation rendererConversation) {
        Vec3d centerDir = rendererConversation.getPosition(other, ticks).add(0, heightOffset, 0).subtract(rendererConversation.getPosition(active, ticks).add(0, heightOffset, 0));
        double distance = centerDir.length() / 2 * this.distance;
        Vec3d center = rendererConversation.getPosition(active, ticks).add(centerDir.x / 2, centerDir.y / 2, centerDir.z / 2);
        Vec3d centerCross = centerDir.normalize().crossProduct(new Vec3d(0, oppositeSide ? -1 : 1, 0)).normalize();
        RayTraceResult hit = MOPhysicsHelper.rayTraceForBlocks(center, active.world, distance, ticks, null, true, true, centerCross);
        Vec3d pos = center.add(centerCross.x * distance, centerCross.y * distance, centerCross.z * distance);
        if (hit != null) {
            pos = hit.hitVec;
        }

        rendererConversation.setCameraPosition(pos.x, pos.y, pos.z);
        rendererConversation.rotateCameraYawTo(centerCross, 90);
        return true;
    }
}
