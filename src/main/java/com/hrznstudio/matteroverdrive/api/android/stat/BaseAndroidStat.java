package com.hrznstudio.matteroverdrive.api.android.stat;

import net.minecraft.util.ResourceLocation;

public abstract class BaseAndroidStat implements IAndroidStat {

    public BaseAndroidStat(ResourceLocation resourceLocation) {
        setRegistryName(resourceLocation);
    }
}
