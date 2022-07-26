package com.teamacronymcoders.matteroverdrive.block;

import com.teamacronymcoders.matteroverdrive.api.misc.RotationType;
import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOPoweredMultiBlock;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOPoweredMultiBlockTile;
import com.teamacronymcoders.matteroverdrive.block.tile.ChargingStationTile;
import com.teamacronymcoders.matteroverdrive.util.MOBlockUtil;
import com.teamacronymcoders.matteroverdrive.util.MOTileHelper;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChargingStationBlock extends MOPoweredMultiBlock<ChargingStationTile> {

    public static final BooleanProperty MIDDLE = BooleanProperty.create("middle");
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public ChargingStationBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), ChargingStationTile.class, true, BlockPos.ZERO);
    }

    //TODO: Figure out why breaking the parent block doesn't break the top blocks >_>
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, livingEntity, stack);
        BlockPos offsetPos = pos;
        MOTileHelper.getTileEntity(level, offsetPos, MOPoweredMultiBlockTile.class).ifPresent(mbt -> {
            level.setBlockAndUpdate(pos, state.setValue(MIDDLE, false).setValue(TOP, false));
            if (mbt.isParent()) {
                mbt.setParentPos(pos);
                mbt.setChanged();
            }
        });
        BlockState placedState = MOBlocks.CHARGING_STATION.get().defaultBlockState().setValue(MIDDLE, true);
        for (int i = 0; i < 2; i++) {
            offsetPos = BlockPos.of(BlockPos.offset(offsetPos.asLong(), Direction.UP));
            if (i == 1) placedState = MOBlocks.CHARGING_STATION.get().defaultBlockState().setValue(TOP, true);
            level.setBlockAndUpdate(offsetPos, placedState);
            MOTileHelper.getTileEntity(level, offsetPos, MOPoweredMultiBlockTile.class).ifPresent(mbt -> {
                mbt.setParentPos(pos);
                mbt.setChanged();
            });
        }
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MIDDLE, TOP);
        super.createBlockStateDefinition(builder);
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
