package com.teamacronymcoders.matteroverdrive.block;

import com.teamacronymcoders.matteroverdrive.api.misc.RotationType;
import com.teamacronymcoders.matteroverdrive.block.extendable.block.MORotatableBlock;
import com.teamacronymcoders.matteroverdrive.block.tile.ChargingStationTile;
import com.teamacronymcoders.matteroverdrive.util.MOBlockUtil;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
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
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), ChargingStationTile.class);
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        BlockPos offsetPos = pos;
        for (int i = 0; i < 2; i++) {
            offsetPos = BlockPos.of(BlockPos.offset(offsetPos.asLong(), Direction.UP));
            level.setBlockAndUpdate(offsetPos, MOBlocks.BOUNDING_BOX.get().defaultBlockState());
            BoundingBoxBlock bbBlock = (BoundingBoxBlock) level.getBlockState(offsetPos).getBlock();
            bbBlock.setParentPos(pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        for (int i = 0; i < 2; i++) {
            BlockPos bbPos = BlockPos.of(BlockPos.offset(i + 1, Direction.UP));
            if (level.getBlockState(bbPos).getBlock() instanceof BoundingBoxBlock) {
                level.setBlock(bbPos, Blocks.AIR.defaultBlockState(), 3);
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
