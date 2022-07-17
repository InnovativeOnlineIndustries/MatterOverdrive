package com.hrznstudio.matteroverdrive.client.gui.element;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.api.android.gui.HudElement;
import com.hrznstudio.matteroverdrive.api.android.gui.HudPosition;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class PerksHudElement extends HudElement {

    public static ResourceLocation BG_BUTTON_ANIM = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/android_feature_icon_bg_active.png");
    public static ResourceLocation BG_BUTTON = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/android_feature_icon_bg.png");

    public PerksHudElement() {
        super("perks", 174, 32);
    }

    @Override
    public boolean isVisible(IAndroid android) {
        return android.isAndroid();
    }

    public static int renderIconWithInfoAnim(PoseStack stack, ResourceLocation holoIcon, Color color, int x, int y, int iconOffsetX, int iconOffsetY, boolean leftSided, int iconWidth, int iconHeight, float backgroundAlpha, float vOffset, int textureX, int textureY) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Screen.fill(stack, x, y - 1, 2 + iconWidth + 2, 18 + 2, new Color(0, 0, 0, backgroundAlpha).getRGB());
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        if (!leftSided) {
            RenderSystem.setShaderTexture(0, holoIcon);
            Screen.blit(stack, x + iconOffsetX, y + iconOffsetY, 100, 0, vOffset, iconWidth, iconHeight, textureY, textureX);
        } else {
            RenderSystem.setShaderTexture(0, holoIcon);
            Screen.blit(stack, x + 2 + iconOffsetX, y + iconOffsetY, 100, 0, vOffset, iconWidth, iconHeight, textureY, textureX);
        }
        RenderSystem.disableBlend();
        return 2 + iconWidth + 2;
    }

    @Override
    public HudPosition getPosition() {
        return HudPosition.TOP_RIGHT;
    }

    private int getIconSize(int iconWidth) {
        return iconWidth + 4;
    }

    @Override
    public void drawElement(PoseStack stack, IAndroid androidPlayer, Window resolution, float ticks) {
        if (!isVisible(androidPlayer)) return;
        stack.pushPose();
        RenderSystem.enableBlend();

        int x = 0;
        int y = 0;
        if (this.getPosition().getY() > 0.5) y = -48;

        if (this.getPosition().getY() == 0 || this.getPosition().getY() == 1) {
            x = 12 - (int) (24 * this.getPosition().getX());
            y = 12 - (int) (24 * this.getPosition().getY());
            //RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
            RenderSystem.setShaderColor(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, baseColor.getAlpha() / 255f);
            RenderSystem.setShaderTexture(0, StatsHudElement.BAR_RL);
            Screen.blit(stack, x, (int) (y + (getHeight(resolution, androidPlayer) - 11) * getPosition().getY()), 0, 0, 174, 11, 174, 11);
            y += 10 - 5 * getPosition().getY();
            x += 5;
            int statsX = x;
            for (String s : androidPlayer.getPerkManager().getOwned().keySet()) {
                if (IAndroidPerk.PERKS.get(s).showOnPlayerHUD(androidPlayer, androidPlayer.getPerkManager().getLevel(IAndroidPerk.PERKS.get(s)))) {
                    statsX -= getIconSize(22);
                }
            }
            statsX += 165 * getPosition().getX();
            for (String s : androidPlayer.getPerkManager().getOwned().keySet()) {
                if (IAndroidPerk.PERKS.get(s).showOnPlayerHUD(androidPlayer, androidPlayer.getPerkManager().getLevel(IAndroidPerk.PERKS.get(s)))) {
                    if (androidPlayer.getPerkManager().getPerkActivityTracker().getOrDefault(s, 0L) + 20 >= Minecraft.getInstance().level.getGameTime()) {
                        renderIconWithInfoAnim(stack, BG_BUTTON_ANIM, baseColor, statsX - 3, y - 3, 0, 0, false, 22, 22, backgroundAlpha, 88 - 22 * ((Minecraft.getInstance().level.getGameTime() / 5) % 4), 22, 88);
                    } else {
                        renderIconWithInfoAnim(stack, BG_BUTTON, baseColor, statsX - 3, y - 3, 0, 0, false, 22, 22, backgroundAlpha, 0, 22, 22);
                    }
                    statsX += StatsHudElement.renderIconWithInfo(stack, IAndroidPerk.PERKS.get(s).getIcon(), "", baseColor, statsX, y, 0, 0, false, 22, 22, backgroundAlpha);
                }
            }

        } else if (getPosition() == HudPosition.MIDDLE_LEFT || getPosition() == HudPosition.MIDDLE_RIGHT) {
            x = 12 - (int) (24 * this.getPosition().getX());
            stack.pushPose();
            //RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
            RenderSystem.setShaderColor(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, baseColor.getAlpha() / 255f);
            stack.translate(x + 11 + (getWidth(resolution, androidPlayer) - 11) * getPosition().getX(), y, 0);
            stack.mulPose(Vector3f.ZP.rotationDegrees(90));
            RenderSystem.setShaderTexture(0, StatsHudElement.BAR_RL);
            Screen.blit(stack, 0, 0, 0, 0, 174, 11, 174, 11);
            stack.popPose();
            y += 86;
            x += 11;
            for (String s : androidPlayer.getPerkManager().getEnabled()) {
                y += StatsHudElement.renderIconWithInfo(stack, IAndroidPerk.PERKS.get(s).getIcon(), "", baseColor, x, y, 0, 0, getPosition() == HudPosition.MIDDLE_LEFT, 16, 16, backgroundAlpha);
            }
        }
        stack.popPose();
    }

}
