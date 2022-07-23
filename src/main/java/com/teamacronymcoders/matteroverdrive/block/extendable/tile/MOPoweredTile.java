package com.teamacronymcoders.matteroverdrive.block.extendable.tile;

import com.google.common.collect.Sets;
import com.teamacronymcoders.matteroverdrive.api.energy.MOEnergyModule;
import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class MOPoweredTile<U extends MOPoweredTile<U>> extends MOBaseTile<U> {

  private final MOEnergyModule<U> energyModule;
  private final LazyOptional<IEnergyStorage> lazyStorage = LazyOptional.of(this::getEnergyStorage);

  private boolean showEnergy = true;

  public MOPoweredTile(MOBaseTileBlock<U> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, Component title) {
    super(base, blockEntityType, pos, state, title);
    this.energyModule = this.createEnergyStorage();
    this.energyModule.setTile(this.getSelf());
  }

  public MOEnergyModule<U> getEnergyStorage() {
    return this.energyModule;
  }

  @Nonnull
  protected MOEnergyModule<U> createEnergyStorage() {
    return new MOEnergyModule<>(10000, 4, 10);
  }

  public Set<Direction> getValidEnergyFaces() {
    return Sets.newHashSet(Direction.values());
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return lazyStorage.cast();
    }
    return super.getCapability(cap, side);
  }

  public void setShowEnergy(boolean showEnergy) {
    this.showEnergy = showEnergy;
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyStorage.invalidate();
  }
}
