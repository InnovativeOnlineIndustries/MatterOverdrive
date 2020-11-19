package com.hrznstudio.matteroverdrive.datagen.lang;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class MOBaseLangProvider extends LanguageProvider {

    public MOBaseLangProvider(DataGenerator gen, String locale) {
        super(gen, MatterOverdrive.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
    }

    /**
     * Helper function to add advancements to the lang generator
     *
     * @param advancement The advancement for localizations to be added
     * @param title       The title of the advancement
     * @param description The description of the advancement
     */
    public void add(Advancement advancement, String title, String description) {
        final DisplayInfo display = advancement.getDisplay();
        add(display.getTitle().getUnformattedComponentText(), title);
        add(display.getDescription().getUnformattedComponentText(), description);
    }

}
