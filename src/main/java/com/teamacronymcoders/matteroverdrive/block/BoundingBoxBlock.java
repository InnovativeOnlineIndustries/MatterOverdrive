package com.teamacronymcoders.matteroverdrive.block;

import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOBaseBlock;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.teamacronymcoders.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BoundingBoxBlock extends MOBaseBlock {

    private final VoxelShape boundingBox;
    private BlockPos parentPos;

    public BoundingBoxBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
        boundingBox = Shapes.block();
    }

    public BoundingBoxBlock(VoxelShape boundingBox) {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
        this.boundingBox = boundingBox;
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
