package com.hrznstudio.matteroverdrive.api.android.perk;

import com.google.common.collect.Multimap;
import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IAndroidPerk {

    public static Map<String, IAndroidPerk> PERKS = new HashMap<>();

    String getName();

    boolean onAndroidTick(IAndroid player, int statLevel);

    void onUnlock(IAndroid player, int statLevel);

    void onUnlearn(IAndroid player, int statLevel);

    boolean canBeUnlocked(IAndroid player, int statLevel);

    boolean showOnPlayerHUD(IAndroid player, int statLevel);

    int getMaxLevel();

    int getRequiredXP(IAndroid player, int statLevel);

    MutableComponent getDisplayName(IAndroid player, int statLevel);

    Multimap<Attribute, AttributeModifier> getAttributeModifiers(IAndroid player, int stateLevel);

    List<ItemStack> getRequiredItems();

    @Nullable
    IAndroidPerk getParent();

    void setParent(IAndroidPerk perk);

    @OnlyIn(Dist.CLIENT)
    void onKeyPress(IAndroid player, int statLevel, int key, boolean down);

    ResourceLocation getIcon();

    @OnlyIn(Dist.CLIENT)
    Point getAndroidStationLocation();

    List<IAndroidPerk> getChild();

    boolean canBeToggled();

}
