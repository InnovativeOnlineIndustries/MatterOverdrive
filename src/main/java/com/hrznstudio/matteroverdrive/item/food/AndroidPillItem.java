package com.hrznstudio.matteroverdrive.item.food;

import com.hrznstudio.matteroverdrive.util.IHasColor;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;

public class AndroidPillItem extends Item implements IHasColor {

    private final int pillColor;

    private final int pillWhite = TextColor.fromTextFormatting(TextFormatting.WHITE).getColor();

    public AndroidPillItem(Properties properties, Color pillColor) {
        super(properties);
        this.pillColor = pillColor.getColor();
    }

    @Override
    public int getColor(int i) {
        return i == 0 ? pillWhite : this.pillColor;
    }
}
