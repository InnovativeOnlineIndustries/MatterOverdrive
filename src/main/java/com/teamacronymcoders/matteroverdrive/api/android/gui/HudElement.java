package com.teamacronymcoders.matteroverdrive.api.android.gui;

import com.teamacronymcoders.matteroverdrive.api.android.IAndroid;
import com.teamacronymcoders.matteroverdrive.reference.ReferenceClient;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import java.awt.*;

public abstract class HudElement implements IHudElement{

    protected Minecraft mc;
    protected String name;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected Color baseColor;
    protected float backgroundAlpha;

    public HudElement(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        mc = Minecraft.getInstance();
        baseColor = ReferenceClient.Colors.HOLO;
    }

    @Override
    public int getWidth(Window resolution, IAndroid androidPlayer) {
        return width;
    }

    @Override
    public int getHeight(Window resolution, IAndroid androidPlayer) {
        return height;
    }

    @Override
    public void setX(int x) {
        this.posX = x;
    }

    @Override
    public void setY(int y) {
        this.posY = y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setBaseColor(Color color) {
        this.baseColor = color;
    }

    public abstract HudPosition getPosition();

    public void setBackgroundAlpha(float alpha) {
        this.backgroundAlpha = alpha;
    }
}
