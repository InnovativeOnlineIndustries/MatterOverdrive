package com.hrznstudio.matteroverdrive.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hrznstudio.matteroverdrive.MatterOverdrive.MOD_ID;

public class MOItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> KUNAI = ITEMS.register("kunai", ()
            -> new TestItem(new Item.Properties().group(ItemGroup.COMBAT)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
