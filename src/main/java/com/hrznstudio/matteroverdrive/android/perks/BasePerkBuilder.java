package com.hrznstudio.matteroverdrive.android.perks;

import com.google.common.collect.Multimap;
import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BasePerkBuilder extends BaseAndroidPerk {

    public BasePerkBuilder(String perkName) {
        super(perkName);
    }

    public BasePerkBuilder onAndroidTick(BiPredicate<IAndroid, Integer> onAndroidTick) {
        this.setOnAndroidTick(onAndroidTick);
        return this;
    }

    public BasePerkBuilder onUnlock(BiConsumer<IAndroid, Integer> onUnlock) {
        this.setOnUnlock(onUnlock);
        return this;
    }

    public BasePerkBuilder onUnlearn(BiConsumer<IAndroid, Integer> onUnlearn) {
        this.setOnUnlearn(onUnlearn);
        return this;
    }

    public BasePerkBuilder canBeUnLocked(BiPredicate<IAndroid, Integer> canBeUnLocked) {
        this.setCanBeUnLocked(canBeUnLocked);
        return this;
    }

    public BasePerkBuilder canShowOnHUD(BiPredicate<IAndroid, Integer> canShowOnHUD) {
        this.setCanShowOnHUD(canShowOnHUD);
        return this;
    }

    public BasePerkBuilder maxLevel(int maxLevel) {
        this.setMaxLevel(maxLevel);
        return this;
    }

    public BasePerkBuilder xpNeeded(int xpNeeded) {
        this.setXpNeeded(xpNeeded);
        return this;
    }

    public BasePerkBuilder attributeModifierMultimap(BiFunction<IAndroid, Integer, Multimap<Attribute, AttributeModifier>> attributeModifierMultimap) {
        this.setAttributeModifierMultimap(attributeModifierMultimap);
        return this;
    }

    public BasePerkBuilder requiredItems(List<ItemStack> requiredItems) {
        this.setRequiredItems(requiredItems);
        return this;
    }

    public BasePerkBuilder parent(IAndroidPerk parent) {
        this.setParent(parent);
        return this;
    }

    public BasePerkBuilder child(IAndroidPerk child) {
        child.setParent(this);
        this.getChild().add(child);
        return this;
    }

    public BasePerkBuilder point(Point point) {
        this.setPoint(point);
        return this;
    }

    public BasePerkBuilder canToggle() {
        this.setCanBeToggled(true);
        return this;
    }

}
