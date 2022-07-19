/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.datagen.client.animation;


import com.hrznstudio.matteroverdrive.datagen.client.animation.segment.AnimationSegmentText;

public class AnimationConsole extends AnimationTimeline<AnimationSegmentText> {

    public AnimationConsole(boolean loopable, int duration) {
        super(loopable, duration);
    }

    public String getString() {
        StringBuilder str = new StringBuilder();
        for (AnimationSegmentText text : getPreviousSegments())
            str.append(text.getText(time)).append("\n");
        AnimationSegmentText segment = getCurrentSegment();
        if (segment != null) {
            str.append(segment.getText(time)).append("\n");
        }
        boolean areAllDone = true;
        for (AnimationSegmentText animationSegmentText : this.getSegments()) {
            if (!animationSegmentText.isAnimationDone(this.time)) {
                areAllDone = false;
                break;
            }
        }
        if (areAllDone)
            str.append(getFinalSegment().getText(time));
        return str.toString();
    }
}
