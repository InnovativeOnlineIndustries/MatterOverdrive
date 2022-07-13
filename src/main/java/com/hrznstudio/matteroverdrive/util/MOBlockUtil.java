package com.hrznstudio.matteroverdrive.util;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MOBlockUtil {

    public static boolean checkDirectionForState(Level level, BlockPos current, Direction direction, Block block, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!level.getBlockState(BlockPos.of(BlockPos.offset( i + 1, direction))).is(block)) return false;
        }
        return true;
    }
}
