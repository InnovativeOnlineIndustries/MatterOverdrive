package com.hrznstudio.matteroverdrive.item;

import com.hrznstudio.matteroverdrive.item.food.AndroidBluePillItem;
import com.hrznstudio.matteroverdrive.item.food.AndroidRedPillItem;
import com.hrznstudio.matteroverdrive.item.food.AndroidYellowPillItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

import static com.hrznstudio.matteroverdrive.MatterOverdrive.MOD_ID;

public class MOItems {

    // For eating or internal damage
    public static final DamageSource NANITES = new DamageSource("nanites").setDamageBypassesArmor();

    public static ItemGroup MATTER_OVERDRIVE = new ItemGroup("matteroverdrive") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ANDROID_PILL_RED.get());
        }
    };

    public static final Food PILLS = (new Food.Builder()).hunger(1).saturation(0.3F).setAlwaysEdible().fastToEat().build();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> ANDROID_PILL_RED = ITEMS.register("android_pill_red", ()
        -> new AndroidRedPillItem(new Item.Properties().food(PILLS).group(MATTER_OVERDRIVE),
        toMCColor(new Color(208, 0, 0, 255))));

    public static final RegistryObject<Item> ANDROID_PILL_BLUE = ITEMS.register("android_pill_blue", ()
        -> new AndroidBluePillItem(new Item.Properties().food(PILLS).group(MATTER_OVERDRIVE),
        toMCColor(new Color(1, 159, 234))));

    public static final RegistryObject<Item> ANDROID_PILL_YELLOW = ITEMS.register("android_pill_yellow", ()
        -> new AndroidYellowPillItem(new Item.Properties().food(PILLS).group(MATTER_OVERDRIVE),
        toMCColor(new Color(255, 228, 0))));

    public static final RegistryObject<Item> KUNAI = ITEMS.register("kunai", ()
        -> new KunaiItem(new Item.Properties().group(ItemGroup.COMBAT).group(MATTER_OVERDRIVE)));

    /**
     * Basically just because i wanted to enable the intellij color picker >:)
     *
     * @param color
     * @return
     */
    public static net.minecraft.util.text.Color toMCColor(Color color) {
        return net.minecraft.util.text.Color.fromHex(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
