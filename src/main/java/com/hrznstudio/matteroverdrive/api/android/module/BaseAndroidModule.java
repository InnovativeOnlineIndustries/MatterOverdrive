package com.hrznstudio.matteroverdrive.api.android.module;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class BaseAndroidModule extends ForgeRegistryEntry<BaseAndroidModule> implements IAndroidModule {

    private final int maxTier;
    private final int minTier;

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
    @OnlyIn(Dist.CLIENT)
    public String getTranslationName() {
        final ResourceLocation id = this.getRegistryName();
        return "module." + id.getNamespace() + "." + id.getPath();
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
