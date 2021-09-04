package com.hrznstudio.matteroverdrive.util;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MOBlockUtil {

    public static boolean checkDirectionForState(World world, BlockPos current, Direction direction, Block state, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!world.getBlockState(current.offset(direction, i + 1)).isIn(state)) return false;
        }
        return true;
    }
}
