package com.teamacronymcoders.matteroverdrive.block.extendable.tile;

import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MOPoweredMultiBlockTile<T extends MOPoweredTile<T>> extends MOPoweredTile<T> {

  private boolean isParent = false;
  private BlockPos parentPos = BlockPos.ZERO;
  private boolean hasFiredRemoved = false;

  public MOPoweredMultiBlockTile(MOBaseTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, Component title) {
    super(base, blockEntityType, pos, state, title);
  }

  @Override
  public void load(CompoundTag compoundTag) {
    if (compoundTag.contains("isParent")) this.setParent(compoundTag.getBoolean("isParent"));
    if (compoundTag.contains("parentPos")) this.setParentPos(NbtUtils.readBlockPos(compoundTag.getCompound("parentPos")));
    if (compoundTag.contains("hasFiredRemoved")) this.setHasFiredRemoved(compoundTag.getBoolean("hasFiredRemoved"));
    super.load(compoundTag);
  }

  @Override
  protected void saveAdditional(CompoundTag compoundTag) {
    compoundTag.putBoolean("isParent", this.isParent());
    if (getParentPos() != null) compoundTag.put("parentPos", NbtUtils.writeBlockPos(this.getParentPos()));
    compoundTag.putBoolean("hasFiredRemoved", this.hasFiredRemoved());
    super.saveAdditional(compoundTag);
  }

  public void setParentPos(BlockPos parentPos) {
    this.parentPos = parentPos;
    this.setChanged();
  }

  public BlockPos getParentPos() {
    return this.parentPos;
  }

  public void setParent(boolean parent) {
    this.isParent = parent;
    this.setChanged();
  }

  public boolean isParent() {
    return this.isParent;
  }

  public void setHasFiredRemoved(boolean hasFiredRemoved) {
    this.hasFiredRemoved = hasFiredRemoved;
    this.setChanged();
  }

  public boolean hasFiredRemoved() {
    return this.hasFiredRemoved;
  }
}
