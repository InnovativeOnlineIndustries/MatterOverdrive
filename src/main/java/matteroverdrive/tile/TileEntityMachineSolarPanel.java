/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.tile;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityMachineSolarPanel extends MOTileEntityMachineEnergy {
    public static final int CHARGE_AMOUNT = 8;
    public static final int ENERGY_STORAGE = 64000;
    public static final int MAX_ENERGY_EXTRACT = 512;

    private byte chargeAmount;

    public TileEntityMachineSolarPanel() {
        super(2);
        energyStorage.setCapacity(ENERGY_STORAGE);
        energyStorage.setMaxExtract(MAX_ENERGY_EXTRACT);
        energyStorage.setMaxReceive(0);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            manageExtract();
            manageChagrgeAmount();
        }

        super.update();
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    protected void manageCharging() {
        if (!world.isRemote) {
            if (isActive()) {
                if (energyStorage.getEnergyStored() < getEnergyStorage().getMaxEnergyStored()) {
                    int energy = energyStorage.getEnergyStored();
                    energy = MathHelper.clamp(energy + getChargeAmount(), 0, energyStorage.getMaxEnergyStored());
                    if (energy != energyStorage.getEnergyStored()) {
                        UpdateClientPower();
                    }
                    getEnergyStorage().setEnergy(energy);
                }
            }
        }
    }

    @Override
    public boolean getServerActive() {
        if (world.provider.hasSkyLight()) {
            boolean i1 = world.canSeeSky(getPos().up());
            float time = getTime();
            if (i1 && time > 0.5) {
                return true;
            }
        }
        return false;
    }

    public void manageExtract() {
        int energy = energyStorage.getEnergyStored();

        if (energy > 0) {
            for (int i = 0; i < 6; i++) {
                int energyToTransfer = Math.min(energy, MAX_ENERGY_EXTRACT);
                if (energyToTransfer > 0) {
                    energy -= MOEnergyHelper.insertEnergyIntoAdjacentEnergyReceiver(this, EnumFacing.VALUES[i], energyToTransfer, false);
                }
            }

            getEnergyStorage().setEnergy(energy);
        }
    }

    public void manageChagrgeAmount() {
        if (!world.isRemote) {
            if (world.provider.hasSkyLight()) {
                float f = 0;
                int i1 = world.getLightFor(EnumSkyBlock.SKY, getPos().up()) - world.getSkylightSubtracted();

                if (i1 >= 15) {
                    f = getTime();
                }

                chargeAmount = (byte) Math.round(CHARGE_AMOUNT * f);
            } else {
                chargeAmount = 0;
            }
        }
    }

    public float getTime() {
        float f = world.getCelestialAngleRadians(1.0F);

        if (f < (float) Math.PI) {
            f += (0.0F - f) * 0.2F;
        } else {
            f += (((float) Math.PI * 2F) - f) * 0.2F;
        }

        return (float) Math.cos(f);
    }

    public byte getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(byte chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return type == UpgradeTypes.PowerStorage;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }
}
