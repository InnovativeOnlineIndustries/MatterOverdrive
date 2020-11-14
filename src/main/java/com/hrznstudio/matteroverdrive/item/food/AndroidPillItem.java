package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.titanium.api.material.IHasColor;
import net.minecraft.item.Item;
import net.minecraft.util.text.Color;

public class AndroidPillItem extends Item implements IHasColor {

    private final int pillColor;

    public AndroidPillItem(Properties properties, Color pillColor) {
        super(properties);
        this.pillColor = pillColor.getColor();
    }

    @Override
    public int getColor(int i) {
        return this.pillColor;
    }
}
