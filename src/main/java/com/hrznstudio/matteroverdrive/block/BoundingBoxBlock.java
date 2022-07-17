package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.tile.BoundingBoxTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.World;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BoundingBoxBlock extends BasicTileBlock<BoundingBoxTile> {

    public BoundingBoxBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), BoundingBoxTile.class);
    }

    @Override
    public IFactory<BoundingBoxTile> getTileEntityFactory() {
        return BoundingBoxTile::new;
    }

    @Override
    public void onReplaced(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        TileUtil.getTileEntity(level, pos, BoundingBoxTile.class).ifPresent(boundingBoxTile -> {
            level.getBlockState(boundingBoxTile.getParent()).onReplaced(level, boundingBoxTile.getParent(), newState, isMoving);
            level.destroyBlock(boundingBoxTile.getParent(), true);
        });
        super.onReplaced(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return TileUtil.getTileEntity(level, pos, BoundingBoxTile.class).map(boundingBoxTile -> TileUtil.getTileEntity(level, boundingBoxTile.getParent(), BasicTile.class).map(basicTile -> basicTile.onActivated(player, hand, ray.getFace(), ray.getHitVec().x, ray.getHitVec().y, ray.getHitVec().z)).orElse(ActionResultType.PASS)).orElse(ActionResultType.FAIL);
    }

    @Override
    public TileEntityType getTileEntityType() {
        return MOBlocks.BOUNDING_BOX_TILE.get();
    }
}
