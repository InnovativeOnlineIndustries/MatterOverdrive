package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BoundingBoxTile extends MOBaseTile<BoundingBoxTile> {

    private BlockPos parent;

    public BoundingBoxTile(BlockPos pos, BlockState state) {
        super(MOBlocks.BOUNDING_BOX.get(), MOBlocks.BOUNDING_BOX_TILE.get(), pos, state, Component.translatable("gui.matteroverdrive.boundingbox"));
    }

    public BlockPos getParent() {
        return parent;
    }

    public void setParent(BlockPos parent) {
        this.parent = parent;
        this.markForUpdate();
    }

    @NotNull
    @Override
    public BoundingBoxTile getSelf() {
        return this;
    }
}
