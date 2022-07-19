/*
 * This file is part of Matter Overdrive: Horizon Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.matteroverdrive.datagen.client.animation;

import com.hrznstudio.matteroverdrive.datagen.client.animation.segment.AnimationSegment;

import java.util.ArrayList;
import java.util.List;

public class AnimationTimeline<T extends AnimationSegment> {

    protected int time;
    private boolean loopable;
    private int duration;
    private List<T> segments;
    private T finalSegment;
    private int lastSegmentBegin;

    public AnimationTimeline(boolean loopable, int duration) {
        segments = new ArrayList<>();
        this.loopable = loopable;
        this.duration = duration;
    }

    public static boolean animationBefore(int time, int begin, int length) {
        return time > begin && time >= begin + length;
    }

    public static boolean animationInRange(int time, int begin, int length) {
        return time >= begin && time < begin + length;
    }

    public double getPercent() {
        return (double) time / (double) duration;
    }

    public void addSegment(T segment) {
        segments.add(segment);
    }

    public void addSegmentSequential(T segment) {
        segment.setBegin(lastSegmentBegin);
        lastSegmentBegin += segment.getLength();
        segments.add(segment);
    }

    public List<T> getSegments() {
        return segments;
    }

    public T getCurrentSegment() {
        for (T segment : segments) {
            if (animationInRange(time, segment.getBegin(), segment.getLength())) {
                return segment;
            }
        }
        return null;
    }

    public List<T> getPreviousSegments() {
        List<T> list = new ArrayList<>();
        for (T segment : segments) {
            if (animationBefore(time, segment.getBegin(), segment.getLength())) {
                list.add(segment);
            }
        }
        return list;
    }

    public void tick() {
        if (time < duration) {
            time++;
        } else if (loopable) {
            time = 0;
        }
    }

    public T getFinalSegment() {
        return finalSegment;
    }

    public void setFinalSegment(T finalSegment) {
        this.finalSegment = finalSegment;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
