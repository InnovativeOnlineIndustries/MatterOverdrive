package com.hrznstudio.matteroverdrive.api.android;

import com.google.common.collect.Multimap;
import com.hrznstudio.matteroverdrive.capabilities.android.PlayerAndroid;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public interface IAndroidStat extends IForgeRegistryEntry<IAndroidStat> {

    void onAndroidTick(PlayerAndroid player, int statLevel);
    void onUnlock(PlayerAndroid player, int statLevel);
    void onUnlearn(PlayerAndroid player, int statLevel);

    boolean canBeUnlocked(PlayerAndroid player, int statLevel);
    boolean showOnPlayerHUD(PlayerAndroid player, int statLevel);

    int getMaxLevel();
    int getRequiredXP(PlayerAndroid player, int statLevel);

    String getDisplayName(PlayerAndroid player, int statLevel);

    Multimap<String, AttributeModifier> getAttributeModifiers(PlayerAndroid player, int stateLevel);

    List<ItemStack> getRequiredItems();

    @Nullable
    IAndroidStat getParent();

    // Client-Side Methods
    @OnlyIn(Dist.CLIENT)
    void onKeyPress(PlayerAndroid player, int statLevel, int key, boolean down);

    //@OnlyIn(Dist.CLIENT)
    // TODO: Implement This
    //IHudElement getIcon(int stateLevel);

    @OnlyIn(Dist.CLIENT)
    Point getAndroidStationLocation();

}
