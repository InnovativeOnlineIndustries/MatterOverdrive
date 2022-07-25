package com.teamacronymcoders.matteroverdrive.util;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MOBlockUtil {

    public static boolean checkDirectionForState(Level level, BlockPos current, Direction direction, Block block, int amount) {
        BlockPos offsetPos = current;
        for (int i = 0; i < amount; i++) {
            offsetPos = BlockPos.of(BlockPos.offset(offsetPos.asLong(), direction));
            if (!level.getBlockState(offsetPos).is(block)) return false;
        }
        return true;
    }
}
