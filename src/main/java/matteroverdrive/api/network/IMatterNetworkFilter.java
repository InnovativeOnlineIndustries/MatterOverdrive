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

package matteroverdrive.api.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 7/22/2015.
 * This is used by machines that have a Matter Network filter.
 * This is mainly used in conjunction with {@link IMatterNetworkBroadcaster} or {@link IMatterNetworkDispatcher} to filter the destinations.
 */
public interface IMatterNetworkFilter {
    /**
     * The NBT Tag name of the connections list.
     */
    String CONNECTIONS_TAG = "CONNECTIONS";

    /**
     * Gets the filter for a given stack filter.
     * This is used to get the filter from the item in the item filter slot of the machine.
     *
     * @param stack the filter stack
     * @return the NBT of the filter.
     * @see matteroverdrive.items.NetworkFlashDrive
     */
    NBTTagCompound getFilter(ItemStack stack);
}
