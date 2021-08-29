package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.tile.AndroidStationTile;
import com.hrznstudio.matteroverdrive.container.AndroidStationContainer;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.container.BasicContainer;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.hrznstudio.matteroverdrive.MatterOverdrive.MOD_ID;

public class MOBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static RegistryObject<BasicTileBlock<AndroidStationTile>> ANDROID_STATION = block("android_station", AndroidStationBlock::new);
    public static RegistryObject<BlockItem> ANDROID_STATION_ITEM = blockItem("android_station", ANDROID_STATION);
    public static RegistryObject<TileEntityType<AndroidStationTile>> ANDROID_STATION_TILE = tile("android_station", AndroidStationTile::new, ANDROID_STATION);
    public static RegistryObject<ContainerType<AndroidStationContainer>> ANDROID_CONTAINER = container("android_station", () -> IForgeContainerType.create(AndroidStationContainer::new));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
        TILE_TYPES.register(eventBus);
        CONTAINER_TYPES.register(eventBus);

        NBTManager.getInstance().scanTileClassForAnnotations(AndroidStationTile.class);
    }

    public static <T extends Block> RegistryObject<T> block(String id, Supplier<T> block) {
        return BLOCKS.register(id, block);
    }

    public static RegistryObject<BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return BLOCK_ITEMS.register(id, () -> new BlockItem(sup.get(), new Item.Properties().group(MOItems.MATTER_OVERDRIVE)));
    }

    public static <T extends TileEntity> RegistryObject<TileEntityType<T>> tile(String id, Supplier<T> supplier, Supplier<? extends Block> sup) {
        return TILE_TYPES.register(id, () -> TileEntityType.Builder.create(supplier, sup.get()).build(null));
    }

    public static <T extends Container> RegistryObject<ContainerType<T>> container(String id, Supplier<ContainerType<T>> sup) {
        return CONTAINER_TYPES.register(id, sup);
    }

}
