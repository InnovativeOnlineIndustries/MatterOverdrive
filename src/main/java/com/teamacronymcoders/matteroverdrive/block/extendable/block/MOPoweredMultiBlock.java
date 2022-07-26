package com.teamacronymcoders.matteroverdrive.block.extendable.block;

import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOPoweredMultiBlockTile;
import com.teamacronymcoders.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class MOPoweredMultiBlock<T extends MOBaseTile<T>> extends MORotatableBlock<T>{

  private boolean isParent;
  private BlockPos parentPos;

  public MOPoweredMultiBlock(Properties properties, Class<T> tileClass, boolean isParent, BlockPos parentPos) {
    super(properties, tileClass);
    this.isParent = isParent;
    this.parentPos = parentPos;
  }

  @Override
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
    super.onPlace(state, level, pos, newState, isMoving);
    MOTileHelper.getTileEntity(level, pos, MOPoweredMultiBlockTile.class).map(mbt -> {
      if (mbt.isParent()) {
        mbt.setParentPos(pos);
      }
      return true;
    }).orElse(false);
  }

  @Override
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
    MOTileHelper.getTileEntity(level, pos, MOPoweredMultiBlockTile.class).map(mbt -> {
      if (mbt.isParent()) {
        BlockPos offsetPos = pos;
        for (int i = 0; i < 2; i++) {
          offsetPos = BlockPos.of(BlockPos.offset(offsetPos.asLong(), Direction.UP));
          if (level.getBlockState(offsetPos).getBlock() instanceof MOPoweredMultiBlock<?>) {
            level.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 3);
          }
        }
      } else if (!mbt.isParent() && !mbt.hasFiredRemoved()) {
        level.getBlockState(mbt.getParentPos()).onRemove(level, mbt.getParentPos(), newState, isMoving); // Call onRemove on Parent
        level.destroyBlock(mbt.getParentPos(), true); // Kill the parent block
        mbt.setHasFiredRemoved(true);
      }
      return true;
    }).orElse(false);
    super.onRemove(state, level, pos, newState, isMoving); // Call super
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    return MOTileHelper.getTileEntity(level, pos, MOPoweredMultiBlockTile.class).map(mbt -> MOTileHelper.getTileEntity(level, mbt.getParentPos(), MOBaseTile.class)
            .map(tile -> tile.onActivated(
                    player,
                    hand,
                    ray.getDirection(),
                    ray.getBlockPos().getX(), ray.getBlockPos().getY(), ray.getBlockPos().getZ()
            )).orElse(InteractionResult.PASS)).orElse(InteractionResult.PASS);
  }
}
