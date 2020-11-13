package com.hrznstudio.matteroverdrive.item;

import com.hrznstudio.matteroverdrive.item.food.AndroidPillItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hrznstudio.matteroverdrive.MatterOverdrive.MOD_ID;

public class MOItems {


    public static ItemGroup MATTER_OVERDRIVE = new ItemGroup("matteroverdrive") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(KUNAI.get());
        }
    };

    public static final Food PILLS = (new Food.Builder()).hunger(1).saturation(0.3F).fastToEat().build();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> KUNAI = ITEMS.register("kunai", ()
            -> new KunaiItem(new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> ANDROID_PILL = ITEMS.register("android_pill", ()
            -> new AndroidPillItem(new Item.Properties().food(PILLS).group(MATTER_OVERDRIVE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
