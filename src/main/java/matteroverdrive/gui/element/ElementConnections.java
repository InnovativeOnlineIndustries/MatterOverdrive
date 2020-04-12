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

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.tile.TileEntityMachinePacketQueue;

import java.util.List;

public class ElementConnections extends MOElementBase {
    TileEntityMachinePacketQueue machine;

    public ElementConnections(MOGuiBase gui, int posX, int posY, int width, int height, TileEntityMachinePacketQueue machine) {
        super(gui, posX, posY, width, height);
        this.machine = machine;
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void init() {

    }

    @Override
    public void addTooltip(List<String> var1, int mouseX, int mouseY) {

    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		/*for (int i = 0;i < 6;i++)
		{
            BlockPos connection = machine.getConnection(i);
            GL11.glColor3f(1, 1, 1);
            int x = 50;
            int y = 42 + i * 19;

            if (connection != null)
            {
                String info = EnumFacing.getOrientation(i).name() + " : " + connection.getTileEntity(machine.getworld()).getBlockType().getLocalizedName();

                MOElementButton.NORMAL_TEXTURE.render(x - 6,y - 6,sizeX + 12,19);
                getFontRenderer().drawString(info,x,y,0xFFFFFF);
            }
            else
            {
                MOElementButton.HOVER_TEXTURE_DARK.render(x - 6,y - 6,80,19);
                getFontRenderer().drawString(EnumFacing.getOrientation(i).name() + " : None", x, y, 0xFFFFFF);
            }
        }*/
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
