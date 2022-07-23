package com.teamacronymcoders.matteroverdrive.block;

import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import com.teamacronymcoders.matteroverdrive.block.tile.AndroidStationTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AndroidStationBlock extends MOBaseTileBlock<AndroidStationTile> {

    public AndroidStationBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK), AndroidStationTile.class);
    }


    @Override
    public BlockEntityType.BlockEntitySupplier<?> getBlockEntityFactory() {
        return AndroidStationTile::new;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if(level.getBlockEntity(blockPos) instanceof AndroidStationTile androidStationTile) {
            androidStationTile.onActivated(player, hand, result.getDirection(), result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
        }
        return InteractionResult.SUCCESS;
    }
}
