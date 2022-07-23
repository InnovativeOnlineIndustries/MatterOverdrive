package com.teamacronymcoders.matteroverdrive.api.android.gui;

import com.teamacronymcoders.matteroverdrive.api.android.IAndroid;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import java.awt.*;

public interface IHudElement {

    boolean isVisible(IAndroid android);

    void drawElement(PoseStack stack, IAndroid androidPlayer, Window resolution, float ticks);

    int getWidth(Window resolution, IAndroid androidPlayer);

    int getHeight(Window resolution, IAndroid androidPlayer);

    void setX(int x);

    void setY(int y);

    void setBaseColor(Color color);

    void setBackgroundAlpha(float alpha);

    HudPosition getPosition();

    String getName();

}
