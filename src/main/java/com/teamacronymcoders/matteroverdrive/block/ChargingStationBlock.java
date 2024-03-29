package com.teamacronymcoders.matteroverdrive.block;

import com.teamacronymcoders.matteroverdrive.api.misc.RotationType;
import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOPoweredMultiBlock;
import com.teamacronymcoders.matteroverdrive.block.extendable.block.MORotatableBlock;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChargingStationBlock extends MOPoweredMultiBlock<ChargingStationTile> {

    public static final BooleanProperty MIDDLE = BooleanProperty.create("middle");
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public ChargingStationBlock(Properties props) {
        super(props, ChargingStationTile.class, true, BlockPos.ZERO);
        this.registerDefaultState(this.stateDefinition.any().setValue(MIDDLE, false).setValue(TOP, false));
    }

    //TODO: Figure out why breaking the parent block doesn't break the top blocks >_>
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, livingEntity, stack);
        BlockPos offsetPos = pos;
        MOTileHelper.getTileEntity(level, offsetPos, MOPoweredMultiBlockTile.class).ifPresent(mbt -> {
            // level.setBlockAndUpdate(pos, state.setValue(MIDDLE, false).setValue(TOP, false));
            if (mbt.isParent()) {
                mbt.setParentPos(pos);
                mbt.setChanged();
            }
        });
        BlockState placedState;
        for (int i = 0; i < 2; i++) {
            offsetPos = BlockPos.of(BlockPos.offset(offsetPos.asLong(), Direction.UP));
            placedState = MOBlocks.CHARGING_STATION.get().defaultBlockState();
            placedState = placedState.setValue(FACING_HORIZONTAL, state.getValue(FACING_HORIZONTAL));
            if (i == 0) placedState = placedState.setValue(MIDDLE, true);
            if (i == 1) placedState = placedState.setValue(TOP, true);
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        Vec3 vector3d = state.getOffset(blockGetter, pos);
        VoxelShape shape = Shapes.or(Block.box(1.56D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D));
        if (state.getValue(MIDDLE).booleanValue()) {
            return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D).move(vector3d.x, vector3d.y, vector3d.z);
        } else if(state.getValue(TOP).booleanValue()) {
            return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D).move(vector3d.x, vector3d.y, vector3d.z);
        }
        return Shapes.or(Block.box(1.56D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)).move(vector3d.x, vector3d.y, vector3d.z);
    }

    @Override
    public float getShadeBrightness(BlockState p_60472_, BlockGetter p_60473_, BlockPos p_60474_) {
        return 1F;
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
