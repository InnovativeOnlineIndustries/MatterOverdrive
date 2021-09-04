package com.hrznstudio.matteroverdrive.capabilities.android;

import com.hrznstudio.matteroverdrive.capabilities.AndroidEnergyCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AndroidEnergyProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    private final AndroidEnergyCapability energyCapability = new AndroidEnergyCapability(AndroidEnergyCapability.DEFAULT_ENERGY);

    private final LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> energyCapability);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityEnergy.ENERGY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("Energy", energyCapability.getEnergyStored());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        energyCapability.setEnergy(nbt.getInt("Energy"));
    }
}
