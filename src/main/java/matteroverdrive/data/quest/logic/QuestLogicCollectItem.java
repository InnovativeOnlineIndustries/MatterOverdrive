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
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

public class QuestLogicCollectItem extends AbstractQuestLogicRandomItem {
    int dimensionID;
    boolean inSpecificDimension;
    boolean destroyOnCollect;
    int xpPerItem;
    int minItemCount;
    int maxItemCount;

    public QuestLogicCollectItem() {
    }

    public QuestLogicCollectItem(QuestItem questItem, int minItemCount, int maxItemCount, int xpPerItem) {
        init(new QuestItem[]{questItem}, minItemCount, maxItemCount, xpPerItem);
    }

    public QuestLogicCollectItem(ItemStack itemStack, int minItemCount, int maxItemCount, int xpPerItem) {
        init(new QuestItem[]{QuestItem.fromItemStack(itemStack)}, minItemCount, maxItemCount, xpPerItem);
    }

    public QuestLogicCollectItem(Item item, int minItemCount, int maxItemCount, int xpPerItem) {
        init(new QuestItem[]{QuestItem.fromItemStack(new ItemStack(item))}, minItemCount, maxItemCount, xpPerItem);
    }

    public QuestLogicCollectItem(ItemStack[] itemStacks, int minItemCount, int maxItemCount, int xpPerItem) {
        QuestItem[] questItems = new QuestItem[itemStacks.length];
        for (int i = 0; i < itemStacks.length; i++) {
            questItems[i] = QuestItem.fromItemStack(itemStacks[i]);
        }
        init(questItems, minItemCount, maxItemCount, xpPerItem);
    }

    public QuestLogicCollectItem(Item[] items, int minItemCount, int maxItemCount, int xpPerItem) {
        QuestItem[] questItems = new QuestItem[items.length];
        for (int i = 0; i < items.length; i++) {
            questItems[i] = QuestItem.fromItemStack(new ItemStack(items[i]));
        }
        init(questItems, minItemCount, maxItemCount, xpPerItem);
    }

    public QuestLogicCollectItem(QuestItem[] questItems, int minItemCount, int maxItemCount, int xpPerItem) {
        init(questItems, minItemCount, maxItemCount, xpPerItem);
    }

    protected void init(QuestItem[] questItems, int minItemCount, int maxItemCount, int xpPerItem) {
        super.init(questItems);
        this.minItemCount = minItemCount;
        this.maxItemCount = maxItemCount;
        this.xpPerItem = xpPerItem;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        super.loadFromJson(jsonObject);
        if (jsonObject.has("dimension")) {
            dimensionID = MOJsonHelper.getInt(jsonObject, "dimension", 0);
            inSpecificDimension = true;
        }
        destroyOnCollect = MOJsonHelper.getBool(jsonObject, "destroy_pickup", false);
        xpPerItem = MOJsonHelper.getInt(jsonObject, "xp", 0);
        minItemCount = MOJsonHelper.getInt(jsonObject, "item_count_min");
        maxItemCount = MOJsonHelper.getInt(jsonObject, "item_count_max");
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        return String.format(info, "", getMaxItemCount(questStack), getItemName(questStack));
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return getItemCount(entityPlayer, questStack) >= getMaxItemCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        return String.format(objective, "", getItemCount(entityPlayer, questStack), getMaxItemCount(questStack), getItemName(questStack));
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack) {
        initTag(questStack);
        initItemType(random, questStack);
        getTag(questStack).setInteger("MaxItemCount", random(random, minItemCount, maxItemCount));
    }

    public int getItemCount(EntityPlayer entityPlayer, QuestStack questStack) {
        if (destroyOnCollect) {
            if (hasTag(questStack)) {
                return getTag(questStack).getInteger("ItemCount");
            }
            return 0;
        } else {
            int itemCount = 0;
            for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++) {
                ItemStack stackInSlot = entityPlayer.inventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    if (matches(questStack, stackInSlot)) {
                        itemCount += stackInSlot.getCount();
                    }
                }
            }
            return itemCount;
        }
    }

    public void setItemCount(QuestStack questStack, int count) {
        if (destroyOnCollect) {
            initTag(questStack);
            getTag(questStack).setInteger("ItemCount", count);
        }
    }

    public int getMaxItemCount(QuestStack questStack) {
        if (hasTag(questStack)) {
            return getTag(questStack).getInteger("MaxItemCount");
        }
        return 0;
    }

    @Override
    public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer) {
        if (destroyOnCollect && event instanceof EntityItemPickupEvent && !((EntityItemPickupEvent) event).getItem().getItem().isEmpty()) {
            if (inSpecificDimension && entityPlayer.world.provider.getDimension() != dimensionID) {
                return null;
            }

            ItemStack itemStack = ((EntityItemPickupEvent) event).getItem().getItem();
            if (!itemStack.isEmpty() && matches(questStack, itemStack)) {
                initTag(questStack);

                int currentItemCount = getItemCount(entityPlayer, questStack);
                if (currentItemCount < getMaxItemCount(questStack)) {
                    setItemCount(questStack, ++currentItemCount);

                    if (isObjectiveCompleted(questStack, entityPlayer, 0)) {
                        markComplete(questStack, entityPlayer);
                        return new QuestLogicState(QuestState.Type.COMPLETE, true);
                    } else {
                        return new QuestLogicState(QuestState.Type.UPDATE, true);
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
        if (!destroyOnCollect) {
            int itemCount = getMaxItemCount(questStack);
            for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++) {
                ItemStack stackInSlot = entityPlayer.inventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    if (matches(questStack, stackInSlot) && itemCount > 0) {
                        int newItemCount = Math.max(0, itemCount - stackInSlot.getCount());
                        int takenFromStack = itemCount - newItemCount;
                        entityPlayer.inventory.decrStackSize(i, takenFromStack);
                        itemCount = newItemCount;
                    }
                }
            }
        }
    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards) {

    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp) {
        return originalXp + getMaxItemCount(questStack) * xpPerItem;
    }

    public QuestLogicCollectItem setDestroyOnCollect(boolean destroyOnCollect) {
        this.destroyOnCollect = destroyOnCollect;
        return this;
    }

    public QuestLogicCollectItem setDimensionID(int dimensionID) {
        this.inSpecificDimension = true;
        this.dimensionID = dimensionID;
        return this;
    }
}
