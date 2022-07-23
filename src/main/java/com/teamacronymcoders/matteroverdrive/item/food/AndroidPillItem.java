package com.teamacronymcoders.matteroverdrive.item.food;

import com.teamacronymcoders.matteroverdrive.util.IHasColor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;

public class AndroidPillItem extends Item implements IHasColor {

    private final int pillColor;

    private final int pillWhite = TextColor.fromLegacyFormat(ChatFormatting.WHITE).getValue();

    public AndroidPillItem(Properties properties, TextColor pillColor) {
        super(properties);
        this.pillColor = pillColor.getValue();
    }

    @Override
    public int getColor(int i) {
        return i == 0 ? pillWhite : this.pillColor;
    }
}
