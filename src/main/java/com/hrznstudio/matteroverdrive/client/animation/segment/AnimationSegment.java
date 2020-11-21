/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.client.animation.segment;

public class AnimationSegment {

    private int begin;
    private int length;

    public AnimationSegment(int begin, int length) {
        this.begin = begin;
        this.length = length;
    }

    public AnimationSegment(int length) {
        this.length = length;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isDone(int time) {
        return (time - this.getBegin()) >= this.getLength();
    }

    public boolean isAnimationDone(int time) {
        return isDone(time);
    }
}
