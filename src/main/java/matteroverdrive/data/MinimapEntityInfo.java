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

package matteroverdrive.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class MinimapEntityInfo {
    private boolean isAttacking;
    private int entityID;

    public MinimapEntityInfo() {
    }

    public MinimapEntityInfo(EntityLivingBase entityLivingBase, EntityPlayer player) {
        if (entityLivingBase instanceof EntityLiving && ((EntityLiving) entityLivingBase).getAttackTarget() != null) {
            isAttacking = player.equals(((EntityLiving) entityLivingBase).getAttackTarget());
        }
        entityID = entityLivingBase.getEntityId();
    }

    public static boolean hasInfo(EntityLivingBase entityLivingBase, EntityPlayer player) {
        return entityLivingBase instanceof EntityLiving && ((EntityLiving) entityLivingBase).getAttackTarget() != null && player.equals(((EntityLiving) entityLivingBase).getAttackTarget());
    }

    public MinimapEntityInfo writeToBuffer(ByteBuf buf) {
        buf.writeBoolean(isAttacking);
        buf.writeInt(entityID);
        return this;
    }

    public MinimapEntityInfo readFromBuffer(ByteBuf buf) {
        isAttacking = buf.readBoolean();
        entityID = buf.readInt();
        return this;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
}
