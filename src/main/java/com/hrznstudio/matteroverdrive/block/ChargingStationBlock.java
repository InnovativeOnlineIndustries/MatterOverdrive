package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.tile.BoundingBoxTile;
import com.hrznstudio.matteroverdrive.block.tile.ChargingStationTile;
import com.hrznstudio.matteroverdrive.util.MOBlockUtil;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChargingStationBlock extends RotatableBlock<ChargingStationTile> {

    public ChargingStationBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), ChargingStationTile.class);
    }

    @Override
    public IFactory<ChargingStationTile> getTileEntityFactory() {
        return ChargingStationTile::new;
    }

    @Override
    public net.minecraft.item.Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) net.minecraft.item.Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public TileEntityType getTileEntityType() {
        return MOBlocks.CHARGING_STATION_TILE.get();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        super.onBlockPlacedBy(world, pos, p_180633_3_, p_180633_4_, p_180633_5_);
        if (!world.isRemote) {
            for (int i = 0; i < 2; i++) {
                world.setBlockState(pos.offset(Direction.UP, i + 1), MOBlocks.BOUNDING_BOX.get().getDefaultState());
                TileUtil.getTileEntity(world, pos.offset(Direction.UP, i + 1), BoundingBoxTile.class).ifPresent(boundingBoxTile -> {
                    boundingBoxTile.setParent(pos);
                });
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, world, pos, newState, isMoving);
        if (!world.isRemote) {
            for (int i = 0; i < 2; i++) {
                world.setBlockState(pos.offset(Direction.UP, i + 1), Blocks.AIR.getDefaultState());
            }
        }
    }

    public static class Item extends BlockItem {

        public Item(Block blockIn, Properties builder) {
            super(blockIn, builder);
        }

        @Override
        protected boolean canPlace(BlockItemUseContext context, BlockState state) {
            return super.canPlace(context, state) && MOBlockUtil.checkDirectionForState(context.getWorld(), context.getPos(), Direction.UP, Blocks.AIR, 2);
        }
    }
}
