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

package matteroverdrive.machines.analyzer;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.util.TimeTracker;

public class ComponentMatterNetworkAnalyzer extends MatterNetworkComponentClient<TileEntityMachineMatterAnalyzer> {
    public static final int TASK_SEARH_DELAY = 40;
    private final TimeTracker taskSearchTimer;

    public ComponentMatterNetworkAnalyzer(TileEntityMachineMatterAnalyzer analyzer) {
        super(analyzer);
        taskSearchTimer = new TimeTracker();
    }

    @Override
    public void onNetworkEvent(IMatterNetworkEvent event) {

    }

    @Override
    public void update() {
        super.update();
        if (!getNodeWorld().isRemote) {
            if (taskSearchTimer.hasDelayPassed(getNodeWorld(), TASK_SEARH_DELAY)) {
                manageTaskSearch();
            }
        }
    }

    private void manageTaskSearch() {
        MatterNetworkTask task = rootClient.getTaskQueue(0).peek();
        if (task != null && task instanceof MatterNetworkTaskStorePattern && getNetwork() != null) {
            getNetwork().post(new IMatterNetworkEvent.Task(task));
            if (task.getState().above(MatterNetworkTaskState.WAITING)) {
                rootClient.getTaskQueue(0).dequeue();
                ComponentTaskProcessingAnalyzer taskProcessingComponent = rootClient.getComponent(ComponentTaskProcessingAnalyzer.class);
                if (taskProcessingComponent != null) {
                    taskProcessingComponent.sendTaskQueueRemovedFromWatchers(task.getId());
                }
                return;
            }
        }
    }
}
