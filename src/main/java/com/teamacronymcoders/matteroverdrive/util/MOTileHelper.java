package com.teamacronymcoders.matteroverdrive.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class MOTileHelper {

  public static <T extends BlockEntity> Optional<T> getTileEntity(BlockGetter access, BlockPos pos, Class<T> tileClass) {
    BlockEntity tile = access.getBlockEntity(pos);
    if (tileClass.isInstance(tile))
      return Optional.of(tileClass.cast(tile));
    return Optional.empty();
  }

  public static Optional<BlockEntity> getTileEntity(BlockGetter access, BlockPos blockPos) {
    return Optional.ofNullable(access.getBlockEntity(blockPos));
  }
}
