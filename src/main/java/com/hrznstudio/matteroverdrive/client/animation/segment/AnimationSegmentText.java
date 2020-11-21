/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.client.animation.segment;

import com.hrznstudio.titanium.util.StringUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.versions.forge.ForgeVersion;

public class AnimationSegmentText extends AnimationSegment {

    private String string;
    private int animationType;

    public AnimationSegmentText(String string, int begin, int length, int animationType) {
        super(begin, length);
        this.string = string;
        this.animationType = animationType;
    }

    public AnimationSegmentText(String string, int length, int animationType) {
        this(string, 0, length, animationType);
    }

    public static AnimationSegmentText getSegmentText(String string, int length, int animationType) {
        if (string.contains("$bar")) return new AnimationSegmentProgressBar(string, length, animationType);
        return new AnimationSegmentText(string, length, animationType);
    }

    public AnimationSegmentText setLengthPerCharacter(double length) {
        this.setLength((int) (string.length() * length));
        return this;
    }

    public String getText(int time) {
        if (animationType == 1) {
            return StringUtil.typingAnimation(TextFormatting.getTextWithoutFormattingCodes(string).replaceAll("\\$gre", TextFormatting.GREEN.toString()).replaceAll("\\$res", TextFormatting.RESET.toString()).replaceAll("\\$forge", ForgeVersion.getVersion()), (time - this.getBegin()), this.getLength());
        }
        return "";
    }

    public void setString(String string) {
        this.string = string;
    }
}
