package com.teamacronymcoders.matteroverdrive.block.extendable.block;

import com.teamacronymcoders.matteroverdrive.api.misc.RotationType;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOBaseTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class MORotatableBlock<T extends MOBaseTile<T>> extends MOBaseTileBlock<T> {

  public static final DirectionProperty FACING_ALL = DirectionProperty.create("facing", Direction.values());
  public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("subfacing", Direction.Plane.HORIZONTAL);

  public MORotatableBlock(Properties properties, Class<T> tileClass) {
    super(properties, tileClass);
  }

  @Nonnull
  public RotationType getRotationType() {
    return RotationType.NONE;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return getRotationType().getHandler().getStateForPlacement(this, context);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    if (this.getRotationType().getProperties() != null) builder.add(this.getRotationType().getProperties());
  }

  @Override
  public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rot) {
    if (getRotationType().getProperties().length > 0) {
      return state.setValue(getRotationType().getProperties()[0], rot.rotate(state.getValue(getRotationType().getProperties()[0])));
    }
    return super.rotate(state, rot);
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    if (this.getRotationType().getProperties().length > 0) {
      return state.rotate(mirror.getRotation(state.getValue(getRotationType().getProperties()[0])));
    }
    return super.mirror(state, mirror);
  }
}
