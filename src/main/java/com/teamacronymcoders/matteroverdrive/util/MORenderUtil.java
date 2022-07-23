package com.teamacronymcoders.matteroverdrive.util;


import java.awt.*;

public class MORenderUtil {

    public static int lerp(int a, int b, float lerp) {
        int MASK1 = 0xff00ff;
        int MASK2 = 0x00ff00;

        int f2 = Math.round(256 * lerp);
        int f1 = Math.round(256 - f2);

        return (((((a & MASK1) * f1) + ((b & MASK1) * f2)) >> 8) & MASK1) | (((((a & MASK2) * f1) + ((b & MASK2) * f2)) >> 8) & MASK2);
    }

    public static Color lerp(Color a, Color b, float lerp) {
        return new Color(lerp(a.getRed(), b.getRed(), lerp), lerp(a.getGreen(), b.getGreen(), lerp), lerp(a.getBlue(), b.getBlue(), lerp), lerp(a.getAlpha(), b.getAlpha(), lerp));
    }

}
