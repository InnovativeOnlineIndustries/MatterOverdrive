package com.hrznstudio.matteroverdrive.datagen.advancements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.matteroverdrive.MatterOverdrive;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.advancements.AdvancementProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ExtendableAdvancementProvider extends AdvancementProvider {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final DataGenerator generator;

    public ExtendableAdvancementProvider(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    @Override
    public void run(CachedOutput cache) {
        Path outputFolder = this.generator.getOutputFolder();
        Consumer<Advancement> consumer = advancement -> {
            Path path = outputFolder.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
            try {
                DataProvider.saveStable(cache, advancement.deconstruct().serializeToJson(), path);
            } catch (IOException e) {
                MatterOverdrive.LOGGER.info(e);
            }
        };
        addAdvancements(consumer);
    }

    protected void addAdvancements(Consumer<Advancement> consumer) {
    }


    @Override
    public String getName() {
        return "MO Advancements";
    }

}
