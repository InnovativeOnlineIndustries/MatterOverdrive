package com.hrznstudio.matteroverdrive.api.android.stat;


import net.minecraft.entity.Entity;
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

    /**
     * Sets the remaining turning time of an android
     *
     * @return the remaining time
     */
    void setTurningTime(int time);

    /**
     * Requests for the full capability to be updated to the client
     */
    void requestUpdate();

    /**
     * Ticks the client of an entity
     * @param entity to be ticked
     */
    void tickClient(Entity entity);

    /**
     * Ticks the server of an entity
     * @param entity to be ticked
     */
    void tickServer(Entity entity);
}
