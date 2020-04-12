/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.data.matter_network.ItemPatternMapping;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ElementMonitorItemPattern extends ElementItemPattern {
    IButtonHandler buttonHandler;
    boolean expanded;

    public ElementMonitorItemPattern(MOGuiBase gui, ItemPatternMapping pattern, IButtonHandler buttonHandler) {
        super(gui, pattern, "big", 22, 22);
        this.buttonHandler = buttonHandler;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        RenderUtils.renderStack(posX + 3, posY + 3, 3, itemStack, true);

        if (!expanded && amount > 0) {
            GlStateManager.pushMatrix();
            GL11.glTranslatef(0, 0, 100);
            gui.drawCenteredString(getFontRenderer(), Integer.toString(amount), posX + 17, posY + 12, 0xFFFFFF);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        super.drawBackground(mouseX, mouseY, gameTicks);

        if (expanded) {
            ApplyColor();
            MOElementButton.NORMAL_TEXTURE.render(posX + 22, posY + 2, 18, 18);
            getFontRenderer().drawString(TextFormatting.BOLD + "+", posX + 28, posY + 7, Reference.COLOR_MATTER.getColor());
            ApplyColor();
            MOElementButton.NORMAL_TEXTURE.render(posX + 22, posY + 22, 18, 18);
            getFontRenderer().drawString(TextFormatting.BOLD + "-", posX + 28, posY + 28, Reference.COLOR_MATTER.getColor());
            ApplyColor();
            MOElementButton.HOVER_TEXTURE_DARK.render(posX + 2, posY + 22, 18, 18);
            gui.drawCenteredString(getFontRenderer(), Integer.toString(amount), posX + 11, posY + 28, Reference.COLOR_MATTER.getColor());
        }
        ResetColor();
    }

    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
        if (expanded) {
            if (mouseX < posX + 22 && mouseY < posY + 22) {
                setExpanded(false);
            } else if (mouseX > posX + 24 && mouseY < posY + 22) {
                amount = Math.min(amount + (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) | Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 16 : 1), 64);
            } else if (mouseX > posX + 24 && mouseY > posY + 24) {
                amount = Math.max(amount - (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) | Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 16 : 1), 0);
            }
        } else {
            setExpanded(true);
        }
        return true;
    }

    public void setExpanded(boolean expanded) {
        if (!expanded) {
            this.expanded = false;
            this.setSize(22, 22);
        } else {
            this.expanded = true;
            this.setSize(44, 44);
        }
    }
}
