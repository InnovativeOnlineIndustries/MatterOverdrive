package com.hrznstudio.matteroverdrive.container;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
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

    private final Container container;

    public AndroidStationMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        super(MOBlocks.ANDROID_CONTAINER.get(), id);
        this.container = inventory;
    }

    public AndroidStationMenu(int id, Inventory inventory) {
        super(MOBlocks.ANDROID_CONTAINER.get(), id);
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
    public boolean stillValid(Player p_38874_) {
        return false;
    }



//    @Override
//    public boolean canInteractWith(Player playerIn) {
//        return playerIn.getCapability(MOCapabilities.ANDROID_DATA).map(IAndroid::isAndroid).orElse(false);
//    }


}
