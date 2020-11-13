package com.hrznstudio.matteroverdrive.api.android;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IAndroid extends ICapabilitySerializable<CompoundNBT> {

    /**
     * Checks if the {@link net.minecraft.entity.player.PlayerEntity} is currently and android or not
     *
     * @return true if the player is an android
     */
    boolean isAndroid();

    /**
     * Checks if the {@link net.minecraft.entity.player.PlayerEntity} is turning into an android
     *
     * @return true if the player is currently turning into an android
     */
    boolean isTurning();

    /**
     * Gets the player that has the capability
     *
     * @return the player
     */
    PlayerEntity getPlayer();
}
