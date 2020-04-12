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

package matteroverdrive.data.quest.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import matteroverdrive.api.events.MOEventDialogConstruct;
import matteroverdrive.api.events.MOEventDialogInteract;
import matteroverdrive.api.exceptions.MORuntimeException;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.dialog.DialogMessage;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

public class QuestLogicConversation extends AbstractQuestLogic {
    String regex;
    String npcType;
    IDialogOption[] given;
    IDialogOption targetOption;

    public QuestLogicConversation() {
    }

    public QuestLogicConversation(String npcType, DialogMessage targetOption, DialogMessage... given) {
        this.npcType = npcType;
        this.targetOption = targetOption;
        this.given = given;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        super.loadFromJson(jsonObject);
        npcType = MOJsonHelper.getString(jsonObject, "npc");
        if (jsonObject.has("given")) {
            JsonArray givenArray = jsonObject.getAsJsonArray("given");
            given = new IDialogOption[givenArray.size()];
            for (int i = 0; i < givenArray.size(); i++) {
                given[i] = MatterOverdrive.DIALOG_ASSEMBLER.parseOption(givenArray.get(i), MatterOverdrive.DIALOG_REGISTRY);
            }
        }
        if (jsonObject.has("target")) {
            targetOption = MatterOverdrive.DIALOG_ASSEMBLER.parseOption(jsonObject.get("target"), MatterOverdrive.DIALOG_REGISTRY);
            if (targetOption == null) {
                throw new MORuntimeException("Conversation Quest Logic mush have a target dialog option");
            }
        }
        regex = MOJsonHelper.getString(jsonObject, "regex", null);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        info = info.replace("$target", MOStringHelper.translateToLocal("entity." + npcType + ".name"));
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return hasTalked(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        objective = objective.replace("$target", MOStringHelper.translateToLocal("entity." + npcType + ".name"));
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack) {

    }

    @Override
    public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer) {
        if (event instanceof MOEventDialogInteract) {
            if (isTarget(((MOEventDialogInteract) event).npc) && targetOption.equalsOption(((MOEventDialogInteract) event).dialogOption)) {
                setTalked(questStack, true);
                markComplete(questStack, entityPlayer);
                return new QuestLogicState(QuestState.Type.COMPLETE, true);
            }
        } else if (event instanceof MOEventDialogConstruct.Post) {
            if (given != null && isTarget(((MOEventDialogConstruct) event).npc)) {
                if (((MOEventDialogConstruct) event).mainMessage instanceof DialogMessage) {
                    for (IDialogOption option : given) {
                        ((DialogMessage) ((MOEventDialogConstruct) event).mainMessage).addOption(option);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards) {

    }

    public boolean isTarget(IDialogNpc npc) {
        EntityLiving entity = npc.getEntity();
        if (regex != null && !entity.getDisplayName().getFormattedText().matches(regex)) {
            return false;
        }

        return npcType.equals(EntityList.getEntityString(entity));
    }

    public boolean hasTalked(QuestStack questStack) {
        if (questStack.getTagCompound() != null) {
            return questStack.getTagCompound().getBoolean("talked");
        }
        return false;
    }

    public void setTalked(QuestStack questStack, boolean talked) {
        if (questStack.getTagCompound() == null) {
            questStack.setTagCompound(new NBTTagCompound());
        }
        questStack.getTagCompound().setBoolean("talked", talked);
    }
}
