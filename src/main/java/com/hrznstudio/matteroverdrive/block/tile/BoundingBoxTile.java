package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.BasicTile;
import net.minecraft.util.math.BlockPos;

public class BoundingBoxTile extends BasicTile<BoundingBoxTile> {

    @Save
    private BlockPos parent;

    public BoundingBoxTile() {
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
