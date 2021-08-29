package com.hrznstudio.matteroverdrive.container;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.container.impl.DisableableSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public class AndroidStationContainer extends BasicInventoryContainer {

    public AndroidStationContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(MOBlocks.ANDROID_CONTAINER.get(), inventory, id);
    }

    public AndroidStationContainer(ContainerType type, PlayerInventory inventory, int id) {
        super(type, inventory, id);
        addHotbarSlots();
    }

    public AndroidStationContainer(ContainerType type, PlayerInventory inventory, int id, IAssetProvider assetProvider) {
        super(type, inventory, id, assetProvider);
    }

    @Override
    public ContainerType<?> getType() {
        return MOBlocks.ANDROID_CONTAINER.get();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.getCapability(MOCapabilities.ANDROID_DATA).map(IAndroid::isAndroid).orElse(false);
    }


}
