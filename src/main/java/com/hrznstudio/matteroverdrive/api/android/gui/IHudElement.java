package com.hrznstudio.matteroverdrive.api.android.gui;

import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;

import java.awt.*;

public interface IHudElement {

    boolean isVisible(IAndroid android);

    void drawElement(MatrixStack stack, IAndroid androidPlayer, MainWindow resolution, float ticks);

    int getWidth(MainWindow resolution, IAndroid androidPlayer);

    int getHeight(MainWindow resolution, IAndroid androidPlayer);

    void setX(int x);

    void setY(int y);

    void setBaseColor(Color color);

    void setBackgroundAlpha(float alpha);

    HudPosition getPosition();

    String getName();

}
