package com.hrznstudio.matteroverdrive.capabilities.android;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAndroidData extends INBTSerializable<CompoundNBT> {
    boolean isAndroid();

    void setAndroid(boolean android);
}
