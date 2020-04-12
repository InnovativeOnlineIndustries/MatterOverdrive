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

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.proxy.ClientProxy;

public class ElementCheckbox extends MOElementButtonScaled {
    String checkboxLabel;
    boolean state;

    public ElementCheckbox(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, boolean state) {
        super(gui, handler, posX, posY, name, 16, 16);
        this.state = state;
        setNormalTexture(MOElementButtonScaled.HOVER_TEXTURE_DARK);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        getFontRenderer().drawString(checkboxLabel, posX + sizeX + 4, posY + sizeY / 2 - getFontRenderer().FONT_HEIGHT / 2, 0xFFFFFF);
        if (state) {
            ClientProxy.holoIcons.renderIcon("tick", posX + sizeX / 2 - 8, posY + sizeY / 2 - 8);
        }
    }

    public void setCheckboxLabel(String checkboxLabel) {
        this.checkboxLabel = checkboxLabel;
    }

    public void onAction(int mouseX, int mouseY, int mouseButton) {
        state = !state;
        buttonHandler.handleElementButtonClick(this, this.getName(), lastMouseButton);
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
