package com.hrznstudio.matteroverdrive.client.gui.element;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.api.android.gui.HudElement;
import com.hrznstudio.matteroverdrive.api.android.gui.HudPosition;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.client.screen.elements.PerkButton;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.vector.Vector3f;

public class PerksHudElement extends HudElement {

    public PerksHudElement() {
        super("perks", 174, 32);
    }

    @Override
    public boolean isVisible(IAndroid android) {
        return android.isAndroid();
    }

    @Override
    public void drawElement(MatrixStack stack, IAndroid androidPlayer, MainWindow resolution, float ticks) {
        if (!isVisible(androidPlayer)) return;
        stack.push();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();

        int x = 0;
        int y = 0;
        if (this.getPosition().getY() > 0.5) y = -48;

        if (this.getPosition().getY() == 0 || this.getPosition().getY() == 1) {
            x = 12 - (int) (24 * this.getPosition().getX());
            y = 12 - (int) (24 * this.getPosition().getY());
            //RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
            RenderSystem.color4f(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, baseColor.getAlpha() / 255f);
            mc.getTextureManager().bindTexture(StatsHudElement.BAR_RL);
            AbstractGui.blit(stack, x, (int) (y + (getHeight(resolution, androidPlayer) - 11) * getPosition().getY()), 0, 0, 174, 11, 174, 11);
            y += 10 - 5 * getPosition().getY();
            x += 5;

            int statsX = x;

            for (int i = 0; i < androidPlayer.getPerkManager().getEnabled().size(); i++) {
                statsX -= getIconSize(18);
            }
            statsX += 165 * getPosition().getX();


            for (String s : androidPlayer.getPerkManager().getEnabled()) {
                StatsHudElement.renderIconWithInfo(stack, PerkButton.BG_BUTTON, "", baseColor, statsX, y, 0, 0, false, 16, 16, backgroundAlpha);
                statsX += StatsHudElement.renderIconWithInfo(stack, IAndroidPerk.PERKS.get(s).getIcon(), "", baseColor, statsX, y, 0, 0, false, 16, 16, backgroundAlpha);
            }

        } else if (getPosition() == HudPosition.MIDDLE_LEFT || getPosition() == HudPosition.MIDDLE_RIGHT) {
            x = 12 - (int) (24 * this.getPosition().getX());
            stack.push();
            //RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
            RenderSystem.color4f(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, baseColor.getAlpha() / 255f);
            stack.translate(x + 11 + (getWidth(resolution, androidPlayer) - 11) * getPosition().getX(), y, 0);
            stack.rotate(Vector3f.ZP.rotationDegrees(90));
            mc.getTextureManager().bindTexture(StatsHudElement.BAR_RL);
            AbstractGui.blit(stack, 0, 0, 0, 0, 174, 11, 174, 11);
            stack.pop();
            y += 86;
            x += 11;
            for (String s : androidPlayer.getPerkManager().getEnabled()) {
                y += StatsHudElement.renderIconWithInfo(stack, IAndroidPerk.PERKS.get(s).getIcon(), "", baseColor, x, y, 0, 0, getPosition() == HudPosition.MIDDLE_LEFT, 16, 16, backgroundAlpha);
            }
        }
        RenderSystem.disableAlphaTest();
        stack.pop();
    }

    @Override
    public HudPosition getPosition() {
        return HudPosition.TOP_RIGHT;
    }

    private int getIconSize(int iconWidth) {
        return iconWidth + 4;
    }

}
