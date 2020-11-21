package com.hrznstudio.matteroverdrive.capabilities.android;

import com.hrznstudio.matteroverdrive.api.android.module.AndroidModule;
import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;

public class AndroidData implements IAndroid {

    public static final int TURNING_TIME = 30 * 21;

    private static final String IS_ANDROID_NBT = "isAndroid";
    private static final String TRANSFORMATION_TIME_NBT = "transformationTime";

    private boolean isAndroid;
    private int transformationTime;
    private boolean needsUpdate;

    public final Map<AndroidModule, Boolean> installedModules;

    public AndroidData() {
        this.isAndroid = false;
        this.transformationTime = 0;
        this.needsUpdate = false;
        this.installedModules = new HashMap<>();
    }

    public boolean isAndroid() {
        return this.isAndroid;
    }

    public void setAndroid(boolean android) {
        this.isAndroid = android;
    }

    @Override
    public boolean isTurning() {
        return this.transformationTime > 0 && !this.isAndroid;
    }

    public int getTurningTime() {
        return this.transformationTime;
    }

    public void setTurningTime(int turningTime) {
        this.transformationTime = turningTime;
    }

    @Override
    public void requestUpdate() {
        this.needsUpdate = true;
    }

    @Override
    public void tickClient(Entity entity) {

    }

    @Override
    public void tickServer(Entity entity) {
        if (this.needsUpdate){
            sync();
        }
    }

    @Override
    public Map<AndroidModule, Boolean> getModules() {
        return this.installedModules;
    }

    public void sync(){

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
