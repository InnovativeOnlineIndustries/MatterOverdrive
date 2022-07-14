package com.hrznstudio.matteroverdrive.datagen;


import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.item.MOItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MOItemModelProvider extends ItemModelProvider {


    public MOItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        MOBlocks.BLOCK_ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(getBlockRL(blockItem.getBlock()).getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + getBlockRL(blockItem.getBlock()).getPath())));
        });
        MOItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(getBlockRL(blockItem.getBlock()).getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + getBlockRL(blockItem.getBlock()).getPath())));
        });
    }

    private ResourceLocation getBlockRL(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

}
