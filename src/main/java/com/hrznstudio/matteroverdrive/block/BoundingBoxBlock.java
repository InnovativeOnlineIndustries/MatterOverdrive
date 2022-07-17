package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.hrznstudio.matteroverdrive.block.tile.BoundingBoxTile;
import com.hrznstudio.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BoundingBoxBlock extends MOBaseTileBlock<BoundingBoxTile> {

    public BoundingBoxBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), BoundingBoxTile.class);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        MOTileHelper.getTileEntity(level, pos, BoundingBoxTile.class).ifPresent(boundingBoxTile -> {
            level.getBlockState(boundingBoxTile.getParent()).onRemove(level, boundingBoxTile.getParent(), newState, isMoving);
            level.destroyBlock(boundingBoxTile.getParent(), true);
        });
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return MOTileHelper.getTileEntity(level, pos, BoundingBoxTile.class).map(boundingBoxTile -> MOTileHelper.getTileEntity(level, boundingBoxTile.getParent(), MOBaseTile.class).map(basicTile -> basicTile.onActivated(player, hand, ray.getDirection(), ray.getBlockPos().getX(), ray.getBlockPos().getY(), ray.getBlockPos().getZ())).orElse(InteractionResult.PASS)).orElse(InteractionResult.FAIL);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getBlockEntityFactory() {
        return BoundingBoxTile::new;
    }
}
