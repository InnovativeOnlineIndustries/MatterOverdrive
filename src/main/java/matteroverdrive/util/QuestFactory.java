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

package matteroverdrive.util;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.quest.IQuest;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class QuestFactory {
    public QuestStack generateQuestStack(Random random, IQuest quest) {
        QuestStack questStack = new QuestStack(quest);
        quest.initQuestStack(random, questStack);
        return questStack;
    }

    @SideOnly(Side.CLIENT)
    public String getFormattedQuestObjective(EntityPlayer entityPlayer, QuestStack questStack, int objectiveInex) {
        boolean isCompleted = questStack.isObjectiveCompleted(entityPlayer, objectiveInex);
        if (isCompleted) {
            //completed
            return TextFormatting.GREEN + Reference.UNICODE_COMPLETED_OBJECTIVE + " " + questStack.getObjective(entityPlayer, objectiveInex);
        } else {
            //not completed
            return TextFormatting.DARK_GREEN + Reference.UNICODE_UNCOMPLETED_OBJECTIVE + " " + questStack.getObjective(entityPlayer, objectiveInex);
        }
    }

    @SideOnly(Side.CLIENT)
    public List<String> getFormattedQuestObjective(EntityPlayer entityPlayer, QuestStack questStack, int objectiveInex, int length) {
        return getFormattedQuestObjective(entityPlayer, questStack, objectiveInex, length, TextFormatting.DARK_GREEN.toString(), TextFormatting.GREEN.toString());
    }

    @SideOnly(Side.CLIENT)
    public List<String> getFormattedQuestObjective(EntityPlayer entityPlayer, QuestStack questStack, int objectiveInex, int length, String uncompletedPrefix, String completedPrefix) {
        List<String> objectiveLines = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(questStack.getObjective(entityPlayer, objectiveInex), length);
        boolean isObjectiveComplete = questStack.isObjectiveCompleted(Minecraft.getMinecraft().player, objectiveInex);
        for (int o = 0; o < objectiveLines.size(); o++) {
            String line = "";
            if (isObjectiveComplete) {
                line += completedPrefix;
                if (o == 0) {
                    line += Reference.UNICODE_COMPLETED_OBJECTIVE + " ";
                }
            } else {
                line += uncompletedPrefix;
                if (o == 0) {
                    line += Reference.UNICODE_UNCOMPLETED_OBJECTIVE + " ";
                }
            }

            line += objectiveLines.get(o);
            objectiveLines.set(o, line);
        }
        return objectiveLines;
    }

    public QuestStack generateQuestStack(String questName) {
        IQuest quest = MatterOverdrive.QUESTS.getQuestByName(questName);
        if (quest != null) {
            QuestStack questStack = new QuestStack(quest);
            quest.initQuestStack(MatterOverdrive.QUESTS.random, questStack);
            return questStack;
        }
        return null;
    }
}
