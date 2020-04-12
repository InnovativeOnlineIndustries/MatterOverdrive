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

public class DialogShotFromBehind extends DialogShot {
    private final float distance;
    private final float sideOffset;

    public DialogShotFromBehind(float distance, float sideOffset) {
        this.distance = distance;
        this.sideOffset = sideOffset;
    }

    @Override
    public boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks, EntityRendererConversation rendererConversation) {
        Vec3d look = rendererConversation.getLook(other, active, ticks);
        double lookDistance = look.length();
        look = new Vec3d(look.x, 0, look.z);
        look = look.normalize();
        Vec3d left = look.crossProduct(new Vec3d(0, 1, 0));
        Vec3d pos = rendererConversation.getPosition(other, ticks).add((left.x * sideOffset) / lookDistance, (left.y * sideOffset) / lookDistance, (left.z * sideOffset) / lookDistance);
        RayTraceResult position = MOPhysicsHelper.rayTrace(pos, other.world, distance, ticks, null, true, false, look, other);
        if (position != null) {
            pos = position.hitVec;
        } else {
            pos.add(look.x * distance, look.y * distance, look.z * distance);
        }
        rendererConversation.setCameraPosition(pos);
        Vec3d rotationLook = pos.subtract(rendererConversation.getPosition(active, ticks)).normalize();
        rendererConversation.rotateCameraYawTo(rotationLook, -90);
        rendererConversation.setCameraPitch(0);
        return true;
    }
}
