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

package matteroverdrive.core;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.MatterOverdriveAPI;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.matter.IMatterRegistry;
import matteroverdrive.api.renderer.IBionicPartRenderRegistry;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.proxy.ClientProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MOAPIInternal implements MatterOverdriveAPI {
    public static final MOAPIInternal INSTANCE = new MOAPIInternal();

    @Override
    public IMatterRegistry getMatterRegistry() {
        return MatterOverdrive.MATTER_REGISTRY;
    }

    @Override
    public IAndroidStatRegistry getAndroidStatRegistry() {
        return MatterOverdrive.STAT_REGISTRY;
    }

    @Override
    public IDialogRegistry getDialogRegistry() {
        return MatterOverdrive.DIALOG_REGISTRY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IAndroidStatRenderRegistry getAndroidStatRenderRegistry() {
        return ClientProxy.renderHandler.getStatRenderRegistry();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBionicPartRenderRegistry getBionicStatRenderRegistry() {
        return ClientProxy.renderHandler.getBionicPartRenderRegistry();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IStarmapRenderRegistry getStarmapRenderRegistry() {
        return ClientProxy.renderHandler.getStarmapRenderRegistry();
    }
}
