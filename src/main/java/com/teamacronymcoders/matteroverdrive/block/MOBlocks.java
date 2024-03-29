package com.teamacronymcoders.matteroverdrive.block;

import com.teamacronymcoders.matteroverdrive.block.tile.AndroidStationTile;
import com.teamacronymcoders.matteroverdrive.block.tile.ChargingStationTile;
import com.teamacronymcoders.matteroverdrive.menu.AndroidStationMenu;
import com.teamacronymcoders.matteroverdrive.item.MOItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.teamacronymcoders.matteroverdrive.MatterOverdrive.MOD_ID;

public class MOBlocks {

    /**
     * Doesn't block light as these are not full blocks
     */
    public static final Material METAL_MACHINE = (new Material.Builder(MaterialColor.METAL).nonSolid().notSolidBlocking()).build();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    //Android Station
    public static RegistryObject<AndroidStationBlock> ANDROID_STATION = block("android_station", AndroidStationBlock::new);
    public static RegistryObject<BlockItem> ANDROID_STATION_ITEM = blockItem("android_station", ANDROID_STATION);
    public static RegistryObject<BlockEntityType<AndroidStationTile>> ANDROID_STATION_TILE = blockEntity("android_station", AndroidStationTile::new, ANDROID_STATION);
    public static RegistryObject<MenuType<AndroidStationMenu>> ANDROID_CONTAINER = container("android_station", () -> IForgeMenuType.create((AndroidStationMenu::new)));

    //Charging Station
    public static RegistryObject<ChargingStationBlock> CHARGING_STATION = block("charging_station", () -> {
        return new ChargingStationBlock(BlockBehaviour.Properties.of(METAL_MACHINE, MaterialColor.METAL).noOcclusion().isSuffocating(MOBlocks::never).isViewBlocking(MOBlocks::never));
    });
    public static RegistryObject<BlockItem> CHARGING_STATION_ITEM = BLOCK_ITEMS.register("charging_station", () -> new ChargingStationBlock.Item(CHARGING_STATION.get(), new Item.Properties().tab(MOItems.MATTER_OVERDRIVE)));
    public static RegistryObject<BlockEntityType<ChargingStationTile>> CHARGING_STATION_TILE = blockEntity("charging_station", ChargingStationTile::new, CHARGING_STATION);

    //Bounding Box
    public static RegistryObject<BoundingBoxBlock> BOUNDING_BOX = block("bounding_box", BoundingBoxBlock::new);


    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
        TILE_TYPES.register(eventBus);
        CONTAINER_TYPES.register(eventBus);

//        NBTManager.getInstance().scanTileClassForAnnotations(AndroidStationTile.class);
//        NBTManager.getInstance().scanTileClassForAnnotations(ChargingStationTile.class);
//        NBTManager.getInstance().scanTileClassForAnnotations(BoundingBoxTile.class);
    }

    public static <T extends Block> RegistryObject<T> block(String id, Supplier<T> block) {
        return BLOCKS.register(id, block);
    }

    public static RegistryObject<BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return BLOCK_ITEMS.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(MOItems.MATTER_OVERDRIVE)));
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> blockEntity(String id, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> sup) {
        return TILE_TYPES.register(id, () -> BlockEntityType.Builder.of(supplier, sup.get()).build(null));
    }

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> container(String id, Supplier<MenuType<T>> sup) {
        return CONTAINER_TYPES.register(id, sup);
    }

}
