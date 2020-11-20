package com.hrznstudio.matteroverdrive.api.android.stat;


import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IAndroid extends ICapabilitySerializable<CompoundNBT> {

    /**
     * Checks if the {@link net.minecraft.entity.Entity} is currently and android or not
     *
     * @return true if the player is an android
     */
    boolean isAndroid();

    /**
     * Checks if the {@link net.minecraft.entity.Entity} is turning into an android
     *
     * @return true if the player is currently turning into an android
     */
    boolean isTurning();

    /**
     * Gets the remaining turning time of an android
     *
     * @return the remaining time
     */
    int getTurningTime();

}
