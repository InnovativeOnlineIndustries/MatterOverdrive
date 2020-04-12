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

package matteroverdrive.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

public class MOEventScan extends PlayerEvent {
    public final ItemStack scannerStack;
    public final RayTraceResult position;
    private final Side side;

    public MOEventScan(EntityPlayer player, ItemStack scannetStack, RayTraceResult position) {
        super(player);
        if (player.world.isRemote) {
            side = Side.CLIENT;
        } else {
            side = Side.SERVER;
        }
        this.scannerStack = scannetStack;
        this.position = position;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
