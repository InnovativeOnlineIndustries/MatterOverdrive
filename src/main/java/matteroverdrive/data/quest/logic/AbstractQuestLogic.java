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

import com.google.gson.JsonObject;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractQuestLogic implements IQuestLogic {
    protected List<IQuestReward> rewards;
    protected boolean autoComplete;
    private String id;

    public AbstractQuestLogic() {
        rewards = new ArrayList<>();
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        this.autoComplete = MOJsonHelper.getBool(jsonObject, "auto_complete", false);
        if (jsonObject.has("rewards")) {
            rewards.addAll(MatterOverdrive.QUEST_ASSEMBLER.parseRewards(jsonObject.getAsJsonArray("rewards")));
        }
    }

    @Override
    public String modifyTitle(QuestStack questStack, String original) {
        return original;
    }

    @Override
    public boolean canAccept(QuestStack questStack, EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer, int count) {
        return count;
    }

    @Override
    public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo) {
        return true;
    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp) {
        return originalXp;
    }

    public int random(Random random, int min, int max) {
        int randomCount = max - min;
        return min + (randomCount > 0 ? random.nextInt(randomCount) : 0);
    }

    protected String getEntityClassName(Class<? extends Entity> entityClass, String unknownTargetName) {
        if (entityClass != null) {
            EntityRegistry.EntityRegistration entityRegistration = EntityRegistry.instance().lookupModSpawn(entityClass, true);
            if (entityRegistration != null) {
                return entityRegistration.getEntityName();
            } else {
                String name = EntityList.getKey(entityClass).toString();
                if (name != null) {
                    return name;
                }
            }
        }
        return unknownTargetName;
    }

    public AbstractQuestLogic setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
        return this;
    }

    @Override
    public String getID() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected boolean hasTag(QuestStack questStack) {
        if (getID() == null) {
            return questStack.getTagCompound() != null;
        } else {
            return questStack.getTagCompound() != null && questStack.getTagCompound().hasKey(getID());
        }
    }

    protected void initTag(QuestStack questStack) {
        if (!hasTag(questStack)) {
            if (getID() == null) {
                questStack.setTagCompound(new NBTTagCompound());
            } else {
                NBTTagCompound tagCompound = questStack.getTagCompound();
                if (tagCompound == null) {
                    tagCompound = new NBTTagCompound();
                }
                tagCompound.setTag(getID(), new NBTTagCompound());
                questStack.setTagCompound(tagCompound);
            }
        }
    }

    protected NBTTagCompound getTag(QuestStack questStack) {
        if (getID() == null) {
            return questStack.getTagCompound();
        } else {
            return questStack.getTagCompound().getCompoundTag(getID());
        }
    }

    protected void markComplete(QuestStack questStack, EntityPlayer entityPlayer) {
        if (autoComplete) {
            questStack.markComplited(entityPlayer, false);
        }

        if (rewards != null) {
            for (IQuestReward reward : rewards) {
                reward.giveReward(questStack, entityPlayer);
            }
        }
    }
}
