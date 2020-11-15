package com.hrznstudio.matteroverdrive.capabilities.android;

import net.minecraft.nbt.CompoundNBT;

public class AndroidData implements IAndroidData {

    public boolean isAndroid() {
        return isAndroid;
    }

    public void setAndroid(boolean android) {
        isAndroid = android;
    }

    private boolean isAndroid;
    private int transformationTime;

    private static final String IS_ANDROID_NBT = "IsAndroid";
    private static final String TRANSFORMATION_TIME_NBT = "TransformationTime";

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean(IS_ANDROID_NBT, this.isAndroid);
        nbt.putInt(TRANSFORMATION_TIME_NBT, this.transformationTime);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.isAndroid = nbt.getBoolean(IS_ANDROID_NBT);
        this.transformationTime = nbt.getInt(TRANSFORMATION_TIME_NBT);
    }
}
