package com.hrznstudio.matteroverdrive.datagen;


import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class MOModelProvider extends BlockModelProvider {

    public MOModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    public void customCrop(CropsBlock block, String name, Integer... filterValues) {
        for (Integer allowedValue : block.getAgeProperty().getAllowedValues()) {
            if (filterValues != null && Arrays.asList(filterValues).contains(allowedValue)) continue;
            getBuilder(block.getRegistryName().getPath() + "_" + allowedValue).parent(getUnchecked(mcLoc(BLOCK_FOLDER + "/crop"))).texture("crop", modLoc(BLOCK_FOLDER + "/" + name + "_stage_" + allowedValue));
        }

    }

    public ModelFile.UncheckedModelFile getUnchecked(ResourceLocation path) {
        ModelFile.UncheckedModelFile ret = new ModelFile.UncheckedModelFile(extendWithFolder(path));
        ret.assertExistence();
        return ret;
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), folder + "/" + rl.getPath());
    }

}
