package com.hrznstudio.matteroverdrive.datagen;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MOBlockstateProvider extends BlockStateProvider {

    public MOBlockstateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockUn(MOBlocks.ANDROID_STATION.get());
    }

    private void crop(CropsBlock block) {
        getVariantBuilder(block).forAllStates(blockState -> {
            int age = blockState.get(block.getAgeProperty());
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + block.getRegistryName().getPath() + "_" + age))).build();
        });
    }

    private void simpleBlockUn(Block block) {
        simpleBlock(block, new ModelFile.UncheckedModelFile(modLoc("block/" + block.getRegistryName().getPath())));
    }

    private void horizontalBlock(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(modLoc("block/" + block.getRegistryName().getPath()));
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(file)
                        .rotationY(((int) state.get(RotatableBlock.FACING_HORIZONTAL).getHorizontalAngle()) % 360)
                        .build()
                );
    }

    private void logBlockRot(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(modLoc("block/" + block.getRegistryName().getPath()));
        getVariantBuilder(block)
                .forAllStates(state -> {
                            Direction.Axis axis = state.get(RotatedPillarBlock.AXIS);
                            if (axis == Direction.Axis.Y) {
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .build();
                            }
                            if (axis == Direction.Axis.Z) {
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationX(90)
                                        .build();
                            }
                            return ConfiguredModel.builder()
                                    .modelFile(file)
                                    .rotationX(90)
                                    .rotationY(90)
                                    .build();
                        }
                );
    }

}
