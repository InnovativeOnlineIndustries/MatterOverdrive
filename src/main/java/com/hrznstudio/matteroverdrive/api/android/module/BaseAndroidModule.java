package com.hrznstudio.matteroverdrive.api.android.module;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BaseAndroidModule extends ForgeRegistryEntry<BaseAndroidModule> implements IAndroidModule {

    private final int maxTier;
    private final int minTier;

    private String translationKey;
    private ITextComponent name;
    private List<ITextComponent> tooltip;

    public BaseAndroidModule() {
        this.maxTier = 1;
        this.minTier = 0;
    }

    public BaseAndroidModule(int maxTier) {
        this.maxTier = maxTier;
        this.minTier = 0;
    }

    public BaseAndroidModule(int maxTier, int minTier) {
        this.maxTier = maxTier;
        this.minTier = minTier;
    }

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
}
