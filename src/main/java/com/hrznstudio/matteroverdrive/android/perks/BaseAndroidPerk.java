package com.hrznstudio.matteroverdrive.android.perks;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BaseAndroidPerk implements IAndroidPerk {

    private BiPredicate<IAndroid, Integer> onAndroidTick = (androidData, integer) -> true;
    private BiConsumer<IAndroid, Integer> onUnlock = (androidData, integer) -> {
    };
    private BiConsumer<IAndroid, Integer> onUnlearn = (androidData, integer) -> {
    };
    private BiPredicate<IAndroid, Integer> canBeUnLocked = (androidData, integer) -> true;
    private BiPredicate<IAndroid, Integer> canShowOnHUD = (androidData, integer) -> false;
    private int maxLevel = 1;
    private int xpNeeded = 0;
    private final String perkName;
    private BiFunction<IAndroid, Integer, Multimap<Attribute, AttributeModifier>> attributeModifierMultimap = (iAndroid, integer) -> ImmutableMultimap.of();
    private List<ItemStack> requiredItems = new ArrayList<>();
    private IAndroidPerk parent = null;
    private List<IAndroidPerk> child = new ArrayList<>();
    private Point point = new Point(0, 0);
    private boolean canBeToggled = false;
    private BiConsumer<IAndroid, Integer> onClientKeyPress = (androidData, integer) -> {
    };

    public BaseAndroidPerk(String perkName) {
        this.perkName = perkName;
        IAndroidPerk.PERKS.put(perkName, this);
    }

    @Override
    public String getName() {
        return perkName;
    }

    @Override
    public boolean onAndroidTick(IAndroid player, int statLevel) {
        return onAndroidTick.test(player, statLevel);
    }

    @Override
    public void onUnlock(IAndroid player, int statLevel) {
        onUnlock.accept(player, statLevel);
    }

    @Override
    public void onUnlearn(IAndroid player, int statLevel) {
        onUnlearn.accept(player, statLevel);
    }

    @Override
    public boolean canBeUnlocked(IAndroid player, int statLevel) {
        return canBeUnLocked.test(player, statLevel) && (getParent() != null && player.getPerkManager().hasPerk(getParent()));
    }

    @Override
    public boolean showOnPlayerHUD(IAndroid player, int statLevel) {
        return canShowOnHUD.test(player, statLevel);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getRequiredXP(IAndroid player, int statLevel) {
        return xpNeeded;
    }

    @Override
    public MutableComponent getDisplayName(IAndroid player, int statLevel) {
        return Component.translatable("matteroverdrive.perk." + perkName + ".name");
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IAndroid player, int stateLevel) {
        return attributeModifierMultimap.apply(player, stateLevel);
    }

    @Override
    public List<ItemStack> getRequiredItems() {
        return requiredItems;
    }

    @Nullable
    @Override
    public IAndroidPerk getParent() {
        return parent;
    }

    @Override
    public void onKeyPress(IAndroid player, int statLevel, int key, boolean down) {
        onClientKeyPress.accept(player, statLevel);
    }

    public void setOnClientKeyPress(BiConsumer<IAndroid, Integer> onClientKeyPress) {
        this.onClientKeyPress = onClientKeyPress;
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/perk/biotic_stat_" + perkName + ".png");
    }

    @Override
    public Point getAndroidStationLocation() {
        return point;
    }

    public List<IAndroidPerk> getChild() {
        return child;
    }

    @Override
    public boolean canBeToggled() {
        return canBeToggled;
    }

    public void setCanBeToggled(boolean canBeToggled) {
        this.canBeToggled = canBeToggled;
    }

    public void setOnAndroidTick(BiPredicate<IAndroid, Integer> onAndroidTick) {
        this.onAndroidTick = onAndroidTick;
    }

    public void setOnUnlock(BiConsumer<IAndroid, Integer> onUnlock) {
        this.onUnlock = onUnlock;
    }

    public void setOnUnlearn(BiConsumer<IAndroid, Integer> onUnlearn) {
        this.onUnlearn = onUnlearn;
    }

    public void setCanBeUnLocked(BiPredicate<IAndroid, Integer> canBeUnLocked) {
        this.canBeUnLocked = canBeUnLocked;
    }

    public void setCanShowOnHUD(BiPredicate<IAndroid, Integer> canShowOnHUD) {
        this.canShowOnHUD = canShowOnHUD;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setXpNeeded(int xpNeeded) {
        this.xpNeeded = xpNeeded;
    }

    public void setAttributeModifierMultimap(BiFunction<IAndroid, Integer, Multimap<Attribute, AttributeModifier>> attributeModifierMultimap) {
        this.attributeModifierMultimap = attributeModifierMultimap;
    }

    public void setRequiredItems(List<ItemStack> requiredItems) {
        this.requiredItems = requiredItems;
    }

    public void setParent(IAndroidPerk parent) {
        this.parent = parent;
    }

    public void setChild(List<IAndroidPerk> child) {
        this.child = child;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
