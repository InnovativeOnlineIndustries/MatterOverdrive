package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.titanium.api.material.IHasColor;
import net.minecraft.item.Item;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

public class AndroidPillItem extends Item implements IHasColor {

    private final int pillColor;

    private final int pillWhite = Color.fromTextFormatting(TextFormatting.WHITE).getColor();

    public AndroidPillItem(Properties properties, Color pillColor) {
        super(properties);
        this.pillColor = pillColor.getColor();
    }

    @Override
    public int getColor(int i) {
        return i == 0 ? pillWhite : this.pillColor ;
    }
}
