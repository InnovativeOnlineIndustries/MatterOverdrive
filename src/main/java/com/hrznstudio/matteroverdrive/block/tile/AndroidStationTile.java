package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOBaseTile;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.container.AndroidStationMenu;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AndroidStationTile extends BaseStationTile<AndroidStationTile> {

    public AndroidStationTile(BlockPos pos, BlockState state) {
        super(MOBlocks.ANDROID_STATION.get(), MOBlocks.ANDROID_STATION_TILE.get(), pos, state, Component.translatable("gui.matteroverdrive.androidstation"));
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction hitDirection, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, hitDirection, hitX, hitY, hitZ) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        openGui(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isUsableByPlayer(LocalPlayer player) {
        return player.getCapability(MOCapabilities.ANDROID_DATA).map(IAndroid::isAndroid).orElse(false);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player p_createMenu_3_) {
        return new AndroidStationMenu(id, inv);
    }

    @NotNull
    @Override
    public AndroidStationTile getSelf() {
        return this;
    }

    public void openGui(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, this, buffer -> {});
        }
    }

}
