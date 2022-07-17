package com.hrznstudio.matteroverdrive.api.android.misc;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface RotationHandler {
  BlockState getStateForPlacement(Block block, BlockPlaceContext context);
}
