package com.hrznstudio.matteroverdrive.client.screen.elements;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.api.android.gui.HudElement;
import com.hrznstudio.matteroverdrive.api.android.gui.HudPosition;
import com.hrznstudio.matteroverdrive.reference.ReferenceClient;
import com.hrznstudio.matteroverdrive.util.MORenderUtil;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.energy.CapabilityEnergy;

import java.awt.*;
import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;

public class StatsHudElement extends HudElement {

    public static final ResourceLocation HEALTH_RL = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "health.png");
    public static final ResourceLocation SPEED_RL = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "speed.png");
    public static final ResourceLocation ENERGY_RL = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "power.png");
    public static final ResourceLocation BAR_RL = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "android_bg_element.png");

    public StatsHudElement() {
        super("android_stats", 174, 32);
    }

    @Override
    public boolean isVisible(IAndroid android) {
        return android.isAndroid();
    }

    public static int renderIconWithInfo(PoseStack stack, ResourceLocation holoIcon, String info, Color color, int x, int y, int iconOffsetX, int iconOffsetY, boolean leftSided, int iconWidth, int iconHeight, float backgroundAlpha) {
        int infoWidth = Minecraft.getInstance().font.width(info);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Screen.fill(stack, x, y - 1, infoWidth + 2 + iconWidth + 2, 18 + 2, new Color(0, 0, 0, backgroundAlpha).getRGB());
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        if (!leftSided) {
            RenderSystem.setShaderTexture(0, holoIcon);
            Screen.blit(stack, x + iconOffsetX, y + iconOffsetY, 100, 0, 0, 16, 16, 16, 16);
            Minecraft.getInstance().font.draw(stack, info, x + iconWidth + 2 + iconOffsetX, y + iconWidth / 2 - Minecraft.getInstance().font.lineHeight / 2 + iconOffsetY, color.getRGB());
        } else {
            Minecraft.getInstance().font.draw(stack, info, x + iconOffsetX, y + iconWidth / 2 - Minecraft.getInstance().font.lineHeight / 2 + iconOffsetY, color.getRGB());
            RenderSystem.setShaderTexture(0, holoIcon);
            Screen.blit(stack, x + infoWidth + 2 + iconOffsetX, y + iconOffsetY, 100, 0, 0, 16, 16, 16, 16);
        }
        RenderSystem.disableBlend();
        return infoWidth + 2 + iconWidth + 2;
    }

    @Override
    public HudPosition getPosition() {
        return HudPosition.TOP_LEFT; //TODO Change with config
    }

    private int getWidthIconWithInfo(String info, int iconWidth) {
        return iconWidth + mc.font.width(info) + 4;
    }

    private int getWidthIconWithPercent(double amount, int iconWidth) {
        return getWidthIconWithInfo(DecimalFormat.getPercentInstance().format(amount), iconWidth);
    }

    @Override
    public void drawElement(PoseStack stack, IAndroid androidPlayer, Window resolution, float ticks) {
        if (!isVisible(androidPlayer)) return;
        stack.pushPose();
        RenderSystem.enableBlend();

        double energy = androidPlayer.getHolder() != null ? androidPlayer.getHolder().getCapability(CapabilityEnergy.ENERGY).map(energyStorage -> energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored()).orElse(0D) : 0;
        double health = mc.player.getHealth() / 20;
        double speed = 1;

        int x = 0;
        int y = 0;
        if (this.getPosition().getY() > 0.5) y = - 48;

        if (this.getPosition().getY() == 0 || this.getPosition().getY() == 1){
            x = 12 - (int) (24 * this.getPosition().getX());
            y = 12 - (int) (24 * this.getPosition().getY());
            //RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
            RenderSystem.setShaderColor(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, baseColor.getAlpha() / 255f);
            RenderSystem.setShaderTexture(0, BAR_RL);
            Screen.blit(stack, x, (int) (y + (getHeight(resolution, androidPlayer) - 11) * getPosition().getY()), 0, 0, 174, 11, 174, 11);
            y += 10 - 5 * getPosition().getY();
            x += 5;

            int statsX = x;
            statsX -= (getWidthIconWithPercent(health, 18) + getWidthIconWithPercent(energy, 20) + getWidthIconWithPercent(1/*androidPlayer.getSpeedMultiply()*/, 16)) * getPosition().getX();
            statsX += 165 * getPosition().getX();

            statsX += renderIconWithPercent(stack, HEALTH_RL, health, statsX, y, 0, 0, false, ReferenceClient.Colors.HOLO_RED, baseColor, 16, 16, backgroundAlpha);
            statsX += renderIconWithPercent(stack, ENERGY_RL, energy, statsX, y, 0, 0, false, ReferenceClient.Colors.HOLO_RED, baseColor, 16, 16, backgroundAlpha);
            renderIconWithPercent(stack, SPEED_RL, speed, statsX, y, 0, 0, false, baseColor, baseColor, 16, 16, backgroundAlpha);
        } else if (getPosition() == HudPosition.MIDDLE_LEFT || getPosition() == HudPosition.MIDDLE_RIGHT){
            x = 12 - (int) (24 * this.getPosition().getX());
            stack.pushPose();
            //RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
            RenderSystem.setShaderColor(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, baseColor.getAlpha() / 255f);
            stack.translate(x + 11 + (getWidth(resolution, androidPlayer) - 11) * getPosition().getX(), y, 0);
            stack.mulPose(Vector3f.ZP.rotationDegrees(90));
            RenderSystem.setShaderTexture(0, BAR_RL);
            Screen.blit(stack, 0,0, 0,0,174, 11, 174, 11);
            stack.popPose();
            y += 86;
            int ySize = 24 + 22 + 24;
            x += 11;
            renderIconWithPercent(stack, HEALTH_RL, health, x + (int) (((getWidth(resolution, androidPlayer) - getWidthIconWithPercent(health, 18)) - 22) * getPosition().getX()), y, 0, 0, false, ReferenceClient.Colors.HOLO_RED, baseColor, 16, 16, backgroundAlpha);
            y += 24;
            renderIconWithPercent(stack, ENERGY_RL, energy, x + (int) (((getWidth(resolution, androidPlayer) - getWidthIconWithPercent(energy, 20)) - 22) * getPosition().getX()), y - 1, 0, 0, false, ReferenceClient.Colors.HOLO_RED, baseColor, 16, 16, backgroundAlpha);
            y += 22;
            renderIconWithPercent(stack, SPEED_RL, speed, x + (int) (((getWidth(resolution, androidPlayer) - getWidthIconWithPercent(speed, 16)) - 22) * getPosition().getX()), y, 0, 0, false, baseColor, baseColor, 16, 16, backgroundAlpha);

        } else if (getPosition() == HudPosition.MIDDLE_CENTER) {
            renderIconWithPercent(stack, HEALTH_RL, health, x - getWidthIconWithPercent(health, 18) - 22, y - 8, 0, 0, true, ReferenceClient.Colors.HOLO_RED, baseColor, 16, 16, backgroundAlpha);
            renderIconWithPercent(stack, ENERGY_RL, energy, x + 24, y - 8, 0, 0, false, ReferenceClient.Colors.HOLO_RED, baseColor, 16, 16, backgroundAlpha);
        }
        stack.popPose();
    }

    private int renderIconWithPercent(PoseStack stack, ResourceLocation holoIcon, double amount, int x, int y, int iconOffsetX, int iconOffsetY, boolean leftSided, Color fromColor, Color toColor, int iconWidth, int iconHeight, float backgroundAlpha) {
        return this.renderIconWithInfo(stack, holoIcon, DecimalFormat.getPercentInstance().format(amount), MORenderUtil.lerp(fromColor, toColor, Mth.clamp((float) amount, 0, 1)), x, y, iconOffsetX, iconOffsetY, leftSided, iconWidth, iconHeight, backgroundAlpha);
    }
}
