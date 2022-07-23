/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.teamacronymcoders.matteroverdrive.client.animation;


import com.teamacronymcoders.matteroverdrive.client.animation.segment.AnimationSegmentText;

public class AnimationTextTyping extends AnimationTimeline<AnimationSegmentText> {

    public AnimationTextTyping(boolean loopable, int duration) {
        super(loopable, duration);
    }

    public String getString() {
        AnimationSegmentText segment = getCurrentSegment();
        if (segment != null) {
            return segment.getText(time);
        }
        return "";
    }
}
