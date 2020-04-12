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

package matteroverdrive.data.dialog;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageQuestStart extends DialogMessage {
    QuestStack questStack;

    public DialogMessageQuestStart() {
        super();
    }

    public DialogMessageQuestStart(QuestStack questStack) {
        super();
        this.questStack = questStack;
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(player);
        return extendedProperties != null && questStack != null && questStack.getQuest().canBeAccepted(questStack, player);
    }

    public DialogMessageQuestStart setQuest(QuestStack questStack) {
        this.questStack = questStack;
        return this;
    }
}
