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

package matteroverdrive.gui.pages;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementTaskList;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;

public class PageTasks extends ElementBaseGroup {
    private ElementTaskList taskList;

    public PageTasks(MOGuiBase gui, int posX, int posY, int width, int height, MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue) {
        super(gui, posX, posY, width, height);
        taskList = new ElementTaskList(gui, gui, 48, 36, 150, 120, taskQueue);
    }

    @Override
    public void init() {
        super.init();
        addElement(taskList);
    }
}
