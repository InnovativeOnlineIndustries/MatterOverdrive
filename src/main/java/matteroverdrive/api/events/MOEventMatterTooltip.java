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
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Simeon on 7/21/2015.
 * This event is triggered when you hold shift while the item tooltip is active.
 * It is connected to showing the matter values for all items.
 * When canceled, the matter info will not be shown.
 */
public class MOEventMatterTooltip extends PlayerEvent {
    public final ItemStack itemStack;
    public final int matter;

    public MOEventMatterTooltip(ItemStack itemStack, int matter, EntityPlayer player) {
        super(player);
        this.itemStack = itemStack;
        this.matter = matter;
    }

    public boolean isCancelable() {
        return true;
    }
}
