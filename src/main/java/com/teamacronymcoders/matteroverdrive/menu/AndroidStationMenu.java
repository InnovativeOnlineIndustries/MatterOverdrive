package com.teamacronymcoders.matteroverdrive.menu;

import com.teamacronymcoders.matteroverdrive.api.android.IAndroid;
import com.teamacronymcoders.matteroverdrive.block.MOBlocks;
import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AndroidStationMenu extends AbstractContainerMenu {

//    public AndroidStationContainer(MenuType type, Inventory inventory, int id) {
//        super(type, inventory, id);
//        addHotbarSlots();
//    }

    private final Inventory inventory;

    public AndroidStationMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        super(MOBlocks.ANDROID_CONTAINER.get(), id);
        this.inventory = inventory;
        // Add Inventory
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }
        // Add Hotbar
        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }
    }

    public AndroidStationMenu(int id, Inventory inventory) {
        super(MOBlocks.ANDROID_CONTAINER.get(), id);
        this.inventory = inventory;
        // Add Inventory
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }
        // Add Hotbar
        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }
    }

    @Override
    public MenuType<?> getType() {
        return MOBlocks.ANDROID_CONTAINER.get();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack ref = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ref = stack.copy();
            if (slotId >= 0 && slotId < 27) {
                if (!this.moveItemStackTo(stack, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId >= 27 && slotId < 36) {
                if (!this.moveItemStackTo(stack, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == ref.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (slotId == 0) {
                player.drop(stack, false);
            }
        }
        return ref;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getCapability(MOCapabilities.ANDROID_DATA).map(IAndroid::isAndroid).orElse(false);
    }

}
