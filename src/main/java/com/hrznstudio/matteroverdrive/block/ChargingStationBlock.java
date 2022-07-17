package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.api.android.misc.RotationType;
import com.hrznstudio.matteroverdrive.block.extendable.block.MORotatableBlock;
import com.hrznstudio.matteroverdrive.block.tile.BoundingBoxTile;
import com.hrznstudio.matteroverdrive.block.tile.ChargingStationTile;
import com.hrznstudio.matteroverdrive.util.MOBlockUtil;
import com.hrznstudio.matteroverdrive.util.MOTileHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChargingStationBlock extends MORotatableBlock<ChargingStationTile> {

    public ChargingStationBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK), ChargingStationTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getBlockEntityFactory() {
        return ChargingStationTile::new;
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }


    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (!world.isClientSide()) {
            for (int i = 0; i < 2; i++) {
                world.setBlock(BlockPos.of(BlockPos.offset(i + 1, Direction.UP)), MOBlocks.BOUNDING_BOX.get().defaultBlockState(), 3);
                MOTileHelper.getTileEntity(world, BlockPos.of(BlockPos.offset(i + 1, Direction.UP)), BoundingBoxTile.class).ifPresent(boundingBoxTile -> {
                    boundingBoxTile.setParent(pos);
                });
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (!level.isClientSide()) {
            for (int i = 0; i < 2; i++) {
                level.setBlock(BlockPos.of(BlockPos.offset(i + 1, Direction.UP)), Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    public static class Item extends BlockItem {

        public Item(Block blockIn, Properties builder) {
            super(blockIn, builder);
        }

        @Override
        protected boolean canPlace(BlockPlaceContext context, BlockState state) {
            return super.canPlace(context, state) && MOBlockUtil.checkDirectionForState(context.getLevel(), context.getClickedPos(), Direction.UP, Blocks.AIR, 2);
        }
    }
}
