package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.s2c.AndroidEnergySyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class AndroidEnergyCapability implements IEnergyStorage {

    public static int DEFAULT_ENERGY = 100_000;
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public AndroidEnergyCapability(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public AndroidEnergyCapability(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public AndroidEnergyCapability(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public AndroidEnergyCapability(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    public static void syncEnergy(ServerPlayer entity) {
        entity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
            PacketHandler.NETWORK.get().sendTo(new AndroidEnergySyncPacket(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored()), entity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(getMaxEnergyStored() - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

}
