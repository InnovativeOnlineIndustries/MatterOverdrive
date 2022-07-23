package com.teamacronymcoders.matteroverdrive.datagen;

import com.teamacronymcoders.matteroverdrive.block.MOBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class MOBlockstateProvider extends BlockStateProvider {

    public MOBlockstateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockUn(MOBlocks.ANDROID_STATION.get());
        horizontalBlock(MOBlocks.ANDROID_STATION.get());
    }

    private void crop(CropBlock block) {
        getVariantBuilder(block).forAllStates(blockState -> {
            int age = blockState.getValue(block.getAgeProperty());
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + getBlockRL(block).getPath() + "_" + age))).build();
        });
    }

    private void simpleBlockUn(Block block) {
        simpleBlock(block, new ModelFile.UncheckedModelFile(modLoc("block/" + getBlockRL(block).getPath())));
    }

    private void horizontalBlock(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(modLoc("block/" + getBlockRL(block).getPath()));
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(file)
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).get3DDataValue()) % 360)
                        .build()
                );
    }

    private void logBlockRot(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(modLoc("block/" + getBlockRL(block).getPath()));
        getVariantBuilder(block)
                .forAllStates(state -> {
                            Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
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

    private ResourceLocation getBlockRL(Block block) {
      return ForgeRegistries.BLOCKS.getKey(block);
    }

}
