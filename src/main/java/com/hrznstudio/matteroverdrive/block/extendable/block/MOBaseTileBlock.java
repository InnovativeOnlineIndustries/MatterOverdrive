package com.hrznstudio.matteroverdrive.block.extendable.block;

import com.hrznstudio.matteroverdrive.block.extendable.tile.ITickableBlockEntity;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.hrznstudio.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class MOBaseTileBlock<T extends MOBaseTile> extends MOBaseBlock implements EntityBlock {

  private final Class<T> tileClass;

  public MOBaseTileBlock(Properties properties, Class<T> tileClass) {
    super(properties);
    this.tileClass = tileClass;
  }

  public abstract BlockEntityType.BlockEntitySupplier<?> getBlockEntityFactory();

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
    getTile(worldIn, pos).ifPresent(tile -> tile.onNeighborChanged(blockIn, fromPos));
  }


  @Override
  @SuppressWarnings("deprecation")
  public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    return getTile(worldIn, pos)
            .map(tile -> tile.onActivated(player, hand, ray.getDirection(), ray.getLocation().x, ray.getLocation().y, ray.getLocation().z))
            .orElseGet(() -> super.use(state, worldIn, pos, player, hand, ray));
  }

  public Optional<T> getTile(BlockGetter level, BlockPos pos) {
    return MOTileHelper.getTileEntity(level, pos, tileClass);
  }

  public Class<T> getTileClass() {
    return tileClass;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level levelIn, BlockState stateIn, BlockEntityType<T> blockEntityIn) {
    return (level, pos, state, blockEntity) -> {
      if (blockEntity instanceof ITickableBlockEntity tickable) {
        if (level.isClientSide()) {
          tickable.clientTick(level, pos, state, blockEntity);
        } else {
          tickable.serverTick(level, pos, state, blockEntity);
        }
      }
    };
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T tile) {
    return EntityBlock.super.getListener(level, tile);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return getBlockEntityFactory().create(pos, state);
  }

}
