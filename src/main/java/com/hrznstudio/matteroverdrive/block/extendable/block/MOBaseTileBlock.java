package com.hrznstudio.matteroverdrive.block.extendable.block;

import com.hrznstudio.matteroverdrive.block.extendable.tile.ITickableBlockEntity;
import com.hrznstudio.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class MOBaseTileBlock<T extends BlockEntity> extends MOBaseBlock implements EntityBlock {

  private final Class<T> tileClass;

  public MOBaseTileBlock(Properties properties, Class<T> tileClass) {
    super(properties);
    this.tileClass = tileClass;
  }

  public abstract BlockEntityType.BlockEntitySupplier<?> getBlockEntityFactory();


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
