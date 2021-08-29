package com.hrznstudio.matteroverdrive.datagen;


import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.item.MOItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MOItemModelProvider extends ItemModelProvider {


    public MOItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, new ExistingFileHelper(Collections.emptyList(), false));
    }

    @Override
    protected void registerModels() {
        MOBlocks.BLOCK_ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(blockItem.getBlock().getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + blockItem.getBlock().getRegistryName().getPath())));
        });
        MOItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(blockItem.getBlock().getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + blockItem.getBlock().getRegistryName().getPath())));
        });
    }

}
