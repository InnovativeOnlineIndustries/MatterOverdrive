package com.hrznstudio.matteroverdrive.api.android.stat;

import com.google.common.collect.Multimap;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public interface IAndroidStat extends IForgeRegistryEntry<IAndroidStat> {

    void onAndroidTick(AndroidData player, int statLevel);

    void onUnlock(AndroidData player, int statLevel);

    void onUnlearn(AndroidData player, int statLevel);

    boolean canBeUnlocked(AndroidData player, int statLevel);

    boolean showOnPlayerHUD(AndroidData player, int statLevel);

    int getMaxLevel();

    int getRequiredXP(AndroidData player, int statLevel);

    String getDisplayName(AndroidData player, int statLevel);

    Multimap<String, AttributeModifier> getAttributeModifiers(AndroidData player, int stateLevel);

    List<ItemStack> getRequiredItems();

    @Nullable
    IAndroidStat getParent();

    // Client-Side Methods
    @OnlyIn(Dist.CLIENT)
    void onKeyPress(AndroidData player, int statLevel, int key, boolean down);

    //@OnlyIn(Dist.CLIENT)
    // TODO: Implement This
    //IHudElement getIcon(int stateLevel);

    @OnlyIn(Dist.CLIENT)
    Point getAndroidStationLocation();

}
