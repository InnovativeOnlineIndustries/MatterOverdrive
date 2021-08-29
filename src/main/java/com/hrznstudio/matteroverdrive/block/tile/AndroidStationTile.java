package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.container.AndroidStationContainer;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.container.BasicContainer;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AndroidStationTile extends BaseStationTile<AndroidStationTile> implements INamedContainerProvider {

    public AndroidStationTile() {
        super(MOBlocks.ANDROID_STATION.get());
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return player.getCapability(MOCapabilities.ANDROID_DATA).map(IAndroid::isAndroid).orElse(false);
    }
    

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new AndroidStationContainer(MOBlocks.ANDROID_CONTAINER.get(), p_createMenu_2_, p_createMenu_1_);
    }

    public void openGui(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, this, buffer -> {});
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("");
    }
}
