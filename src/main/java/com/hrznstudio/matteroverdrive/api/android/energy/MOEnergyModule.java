package com.hrznstudio.matteroverdrive.api.android.energy;

import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import net.minecraftforge.energy.EnergyStorage;

public class MOEnergyModule<T extends MOBaseTile<T>> extends EnergyStorage {

  protected T tile;

  public MOEnergyModule(T tile, int capacity) {
    super(capacity);
  }

  public MOEnergyModule(int capacity, int maxTransfer) {
    super(capacity, maxTransfer, maxTransfer, 0);
  }

  public MOEnergyModule(int capacity, int maxReceive, int maxExtract) {
    super(capacity, maxReceive, maxExtract, 0);
  }

  public MOEnergyModule(int capacity, int maxReceive, int maxExtract, int initialEnergy) {
    super(capacity, maxReceive, maxExtract, initialEnergy);
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    int amount = super.receiveEnergy(maxReceive, simulate);
    if (!simulate && amount > 0) {
      this.tile.markForUpdate();
    }
    return amount;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    int amount = super.extractEnergy(maxExtract, simulate);
    if (!simulate && amount > 0) {
      this.tile.markForUpdate();
    }
    return amount;
  }

  public void setEnergyStored(int energy) {
    if (energy > this.getMaxEnergyStored()) {
      this.energy = this.getMaxEnergyStored();
    } else {
      this.energy = Math.max(energy, 0);
    }
    this.tile.markForUpdate();
  }

  public void setTile(T tile) {
    this.tile = tile;
  }
}
