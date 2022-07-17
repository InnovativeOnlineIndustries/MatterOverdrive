package com.hrznstudio.matteroverdrive.api.android.misc;

import com.hrznstudio.matteroverdrive.block.extendable.block.MORotatableBlock;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public enum RotationType {
  NONE((block, context) -> block.defaultBlockState()),
  FOUR_WAY(((block, context) -> block.defaultBlockState().setValue(MORotatableBlock.FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite())), MORotatableBlock.FACING_HORIZONTAL),
  SIX_WAY((block, context) -> block.defaultBlockState().setValue(MORotatableBlock.FACING_ALL, context.getNearestLookingDirection().getOpposite()), MORotatableBlock.FACING_ALL),
  TWENTY_FOUR_WAY((block, context) -> {
    //TODO: Sub facing
    return block.defaultBlockState().setValue(MORotatableBlock.FACING_ALL, context.getNearestLookingDirection());
  }, MORotatableBlock.FACING_ALL, MORotatableBlock.FACING_HORIZONTAL);

  private final RotationHandler handler;
  private final DirectionProperty[] properties;

  RotationType(RotationHandler handler, DirectionProperty... properties) {
    this.handler = handler;
    this.properties = properties;
  }

  public RotationHandler getHandler() {
    return handler;
  }

  public DirectionProperty[] getProperties() {
    return properties;
  }
}
