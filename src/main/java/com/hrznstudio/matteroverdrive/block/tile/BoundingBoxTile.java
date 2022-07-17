package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.hrznstudio.titanium.annotation.Save;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BoundingBoxTile extends MOBaseTile<BoundingBoxTile> {

    @Save
    private BlockPos parent;

    public BoundingBoxTile(BlockPos pos, BlockState state) {
        super(MOBlocks.BOUNDING_BOX.get());
    }

    public BlockPos getParent() {
        return parent;
    }

    public void setParent(BlockPos parent) {
        this.parent = parent;
        markDirty();
    }
}
