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

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogQuestGiver;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.gui.GuiDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DialogMessageQuestGive extends DialogMessage {
    QuestStack questStack;
    boolean returnToMain;

    public DialogMessageQuestGive(JsonObject object) {
        super(object);
        this.questStack = questStack;
    }

    public DialogMessageQuestGive(String message, QuestStack questStack) {
        super(message);
        this.questStack = questStack;
    }

    public DialogMessageQuestGive(String message, String question, QuestStack questStack) {
        super(message, question);
        this.questStack = questStack;
    }

    public DialogMessageQuestGive(String[] messages, String[] questions, QuestStack questStack) {
        super(messages, questions);
        this.questStack = questStack;
    }

    @Override
    public void onInteract(IDialogNpc npc, EntityPlayer player) {
        super.onInteract(npc, player);
        if (npc != null && npc instanceof IDialogQuestGiver && player != null && !player.world.isRemote) {
            ((IDialogQuestGiver) npc).giveQuest(this, questStack, player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void setAsGuiActiveMessage(IDialogNpc npc, EntityPlayer player) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog) {
            ((GuiDialog) Minecraft.getMinecraft().currentScreen).setCurrentMessage(returnToMain ? npc.getStartDialogMessage(player) : this);
        }
    }

    public DialogMessageQuestGive setReturnToMain(boolean returnToMain) {
        this.returnToMain = returnToMain;
        return this;
    }
}
