package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class BaseStationTile<T extends BaseStationTile<T>> extends MOBaseTile<T> implements MenuProvider, Nameable {

    public BaseStationTile(MOBaseTileBlock<T> block, BlockEntityType<? extends T> type, BlockPos pos, BlockState state, Component title) {
        super(block, type, pos, state, title);
    }

    public boolean isUsableByPlayer(LocalPlayer player){
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
