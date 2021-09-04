package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.tile.BoundingBoxTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BoundingBoxBlock extends BasicTileBlock<BoundingBoxTile> {

    public BoundingBoxBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), BoundingBoxTile.class);
    }

    @Override
    public IFactory<BoundingBoxTile> getTileEntityFactory() {
        return BoundingBoxTile::new;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileUtil.getTileEntity(worldIn, pos, BoundingBoxTile.class).ifPresent(boundingBoxTile -> {
            worldIn.getBlockState(boundingBoxTile.getParent()).onReplaced(worldIn, boundingBoxTile.getParent(), newState, isMoving);
            worldIn.destroyBlock(boundingBoxTile.getParent(), true);
        });
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        return TileUtil.getTileEntity(worldIn, pos, BoundingBoxTile.class).map(boundingBoxTile -> TileUtil.getTileEntity(worldIn, boundingBoxTile.getParent(), BasicTile.class).map(basicTile -> basicTile.onActivated(player, hand, ray.getFace(), ray.getHitVec().x, ray.getHitVec().y, ray.getHitVec().z)).orElse(ActionResultType.PASS)).orElse(ActionResultType.FAIL);
    }

    @Override
    public TileEntityType getTileEntityType() {
        return MOBlocks.BOUNDING_BOX_TILE.get();
    }
}
