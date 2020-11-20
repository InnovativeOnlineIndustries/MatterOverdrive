package com.hrznstudio.matteroverdrive.capabilities.android;

import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class AndroidData implements IAndroid {

    public static final int TURNING_TIME = 30 * 21;

    private static final String IS_ANDROID_NBT = "IsAndroid";
    private static final String TRANSFORMATION_TIME_NBT = "TransformationTime";

    private boolean isAndroid;
    private int transformationTime;

    public AndroidData() {
        this.isAndroid = false;
        this.transformationTime = 0;
    }

    public boolean isAndroid() {
        return isAndroid;
    }

    public void setAndroid(boolean android) {
        isAndroid = android;
    }

    @Override
    public boolean isTurning() {
        return transformationTime > 0 && !isAndroid;
    }

    public int getTurningTime() {
        return transformationTime;
    }

    public void setTurningTime(int turningTime) {
        this.transformationTime = turningTime;
    }

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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == MOCapabilities.ANDROID_DATA ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }
}
