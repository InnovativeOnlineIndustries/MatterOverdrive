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

package matteroverdrive.entity.tasks;

import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkToPlayer extends EntityAIBase {
    private IDialogNpc npc;

    public EntityAITalkToPlayer(IDialogNpc npc) {
        this.npc = npc;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.npc.getEntity().isEntityAlive()) {
            return false;
        } else {
            EntityPlayer entityplayer = this.npc.getDialogPlayer();
            return entityplayer != null && (!(this.npc.getEntity().getDistanceSq(entityplayer) > 32.0D) && entityplayer.openContainer != null);
        }
    }

    @Override
    public void startExecuting() {
        this.npc.getEntity().getNavigator().clearPath();
    }

    public void resetTask() {
        this.npc.setDialogPlayer(null);
    }
}
