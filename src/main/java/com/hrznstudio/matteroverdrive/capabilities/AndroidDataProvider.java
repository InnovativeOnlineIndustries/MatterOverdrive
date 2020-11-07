package com.hrznstudio.matteroverdrive.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AndroidDataProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    private final IAndroidData androidData = MOCapabilities.ANDROID_DATA.getDefaultInstance();

    private final LazyOptional<IAndroidData> optional = LazyOptional.of(() -> androidData);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == MOCapabilities.ANDROID_DATA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return androidData.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        androidData.deserializeNBT(nbt);
    }
}
