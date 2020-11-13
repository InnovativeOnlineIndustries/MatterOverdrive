package com.hrznstudio.matteroverdrive.capabilities.android;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.titanium.network.NetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// https://bitbucket.org/hrznstudio/mo-horizon-edition/src/master/src/main/java/com/hrznstudio/matteroverdrive/capability/android/AndroidPlayer.java
public class PlayerAndroid implements IAndroid {

    public static final int TURNING_TIME = 30 * 21;

    private boolean isAndroid;
    private int turningTime;

    private PlayerEntity player;
    private boolean needsUpdate;

    public PlayerAndroid(PlayerEntity player) {
        this.isAndroid = false;
        this.turningTime = 0;
        this.player = player;
        this.needsUpdate = false;
    }

    @Override
    public boolean isAndroid() {
        return isAndroid;
    }

    @Override
    public boolean isTurning() {
        return turningTime > 0 && !isAndroid;
    }

    public int getTurningTime() {
        return turningTime;
    }

    public void setTurningTime(int turningTime) {
        this.turningTime = turningTime;
    }

    @Override
    public PlayerEntity getPlayer() {
        return player;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        //TODO: Implement
        return LazyOptional.empty();
    }

    public void startTurning() {
        this.turningTime = TURNING_TIME;
    }

    public void tickAndroidServer() {
        //TODO: Implement
    }

    @OnlyIn(Dist.CLIENT)
    public void tickAndroidClient() {
        //TODO: Implement
    }

    private void tickTurningTime() {
        //TODO: Implement
    }

    @Override
    public CompoundNBT serializeNBT() {
        //TODO: Implement
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        //TODO: Implement
    }
}
