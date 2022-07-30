package com.teamacronymcoders.matteroverdrive.block.tile;

import com.teamacronymcoders.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOBaseTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseStationTile<T extends BaseStationTile<T>> extends MOBaseTile<T> implements MenuProvider, Nameable {

    public BaseStationTile(MOBaseTileBlock<T> block, BlockEntityType<? extends T> type, BlockPos pos, BlockState state, Component title) {
        super(block, type, pos, state, title);
    }

    public boolean isUsableByPlayer(Player player){
        return true;
    }

    @Override
    public Component getName() {
        return getTitle();
    }

    @Override
    public Component getDisplayName() {
        return getTitle();
    }

}
