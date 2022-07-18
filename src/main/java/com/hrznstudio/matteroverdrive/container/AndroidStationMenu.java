package com.hrznstudio.matteroverdrive.container;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class AndroidStationMenu extends AbstractContainerMenu {

//    public AndroidStationContainer(MenuType type, Inventory inventory, int id) {
//        super(type, inventory, id);
//        addHotbarSlots();
//    }

    private final Container inventory;

    public AndroidStationMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        super(MOBlocks.ANDROID_CONTAINER.get(), id);
        this.inventory = inventory;
    }

    public AndroidStationMenu(int id, Inventory inventory) {
        super(MOBlocks.ANDROID_CONTAINER.get(), id);
        this.inventory = inventory;
    }

    @Override
    public MenuType<?> getType() {
        return MOBlocks.ANDROID_CONTAINER.get();
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getCapability(MOCapabilities.ANDROID_DATA).map(IAndroid::isAndroid).orElse(false);
    }




}
