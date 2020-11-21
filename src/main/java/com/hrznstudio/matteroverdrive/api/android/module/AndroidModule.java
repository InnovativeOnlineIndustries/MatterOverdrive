package com.hrznstudio.matteroverdrive.api.android.module;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AndroidModule extends ForgeRegistryEntry<AndroidModule> {

    private final int maxTier;
    private final int minTier;
    private final boolean shouldAllowStackingModules;

    private String translationKey;
    private ITextComponent name;
    private List<ITextComponent> tooltip;
    private int currentTier;

    public AndroidModule() {
        this.maxTier = 1;
        this.minTier = 0;
        this.shouldAllowStackingModules = false;
    }

    public AndroidModule(int maxTier) {
        this.maxTier = maxTier;
        this.minTier = 0;
        this.shouldAllowStackingModules = true;
    }

    public AndroidModule(int maxTier, int minTier) {
        this.maxTier = maxTier;
        this.minTier = minTier;
        this.shouldAllowStackingModules = true;
    }

    /**
     * This returns a boolean check against this modifier.
     *
     * @param module The module being passed to check against this.
     * @return Returns if the passed module can be applied with this.
     */
    public boolean canApplyTogether(AndroidModule module) {
        return shouldAllowStackingModules ? this.getCurrentTier() + 1 <= this.getMaxTier() : module != this;
    };

    /**
     * This is used to set the stored data using a NBT-tag on Install.
     *
     * @param nbt The NBT being passed for updating the Module.
     */
    public void onInstall(LivingEntity entity, AndroidData data, CompoundNBT nbt) {}

    /**
     * This is used to update the Modules internal data using passed NBT-Data.
     *
     * @param nbt The NBT being passed for updating the Module.
     */
    public void onUpdate(LivingEntity entity, AndroidData data, CompoundNBT nbt) {}

    /**
     * This is used to set the stored data using a NBT-tag on Removal.
     *
     * @param nbt The NBT being passed for updating the Module.
     */
    public void onRemoval(LivingEntity entity, AndroidData data, CompoundNBT nbt) {}

    /**
     * This returns a boolean check against both Modifiers not just this Modifier.
     *
     * @param module Modifier to check against.
     * @return Returns the final value if this can be applied together with the other Modifier.
     */
    public boolean isCompatibleWith(AndroidModule module) {
        return this.canApplyTogether(module) && module.canApplyTogether(this);
    }

    /**
     * @return Returns the list of AttributeModifiers, that this module should apply.
     */
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        return HashMultimap.create();
    }

    /**
     * @return Returns the Translation Key for the Modifier.
     */
    @Nonnull
    public String getTranslationName() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("android.module", this.getRegistryName());
        }
        return translationKey;
    }

    /**
     * @return Returns the translated Name for the Modifier.
     */
    @Nonnull
    public ITextComponent getName() {
        if (name == null) {
            name = new TranslationTextComponent(this.getTranslationName());
        }
        return name;
    }

    /**
     * @return Returns the translated Name for the Modifier.
     */
    @Nonnull
    public List<ITextComponent> getTooltip() {
        if (tooltip == null) {
            tooltip = Lists.newArrayList();
        }
        return tooltip;
    }

    /**
     * @return Returns the minimum tier-level of the Module.
     */
    public int getMinTier() {
        return minTier;
    }

    /**
     * @return Returns the maximum tier-level of the Module.
     */
    public int getMaxTier() {
        return maxTier;
    }

    /**
     * @param level The tier level being passed pre-setting but post-change.
     * @return Returns if the changed level falls within the bounds of minimum and maximum tier.
     */
    public int getTierInRange(int level) {
        return Math.max(Math.min(level, this.getMaxTier()), this.getMinTier());
    }

    /**
     * @return Gets the current Tier-Level
     */
    public int getCurrentTier() {
        return currentTier;
    }
}
