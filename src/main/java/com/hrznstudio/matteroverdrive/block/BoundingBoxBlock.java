package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.extendable.block.MOBaseBlock;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.hrznstudio.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BoundingBoxBlock extends MOBaseBlock {

    private BlockPos parentPos;

    public BoundingBoxBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        level.getBlockState(this.parentPos).onRemove(level, this.parentPos, newState, isMoving);
        level.destroyBlock(this.parentPos, true);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return MOTileHelper.getTileEntity(level, this.parentPos, MOBaseTile.class).map(basicTile -> basicTile.onActivated(player, hand, ray.getDirection(), ray.getBlockPos().getX(), ray.getBlockPos().getY(), ray.getBlockPos().getZ())).orElse(InteractionResult.PASS);
    }

    public void setParentPos(BlockPos parentPos) {
        this.parentPos = parentPos;
    }

}
