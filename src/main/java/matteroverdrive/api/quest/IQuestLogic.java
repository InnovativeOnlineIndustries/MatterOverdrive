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

package matteroverdrive.api.quest;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

public interface IQuestLogic {
    void loadFromJson(JsonObject jsonObject);

    String modifyTitle(QuestStack questStack, String original);

    boolean canAccept(QuestStack questStack, EntityPlayer entityPlayer);

    String modifyInfo(QuestStack questStack, String info);

    boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex);

    String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex);

    int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer, int count);

    void initQuestStack(Random random, QuestStack questStack);

    QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer);

    boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo);

    void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer);

    void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer);

    int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp);

    void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards);

    String getID();
}
