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

package matteroverdrive.api.inventory;

import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by Simeon on 4/11/2015.
 * This is used by Machines to increase stats.
 */
public interface IUpgrade {
    /**
     * A map of all the Upgrade Stats the Upgrade changes.
     *
     * @param itemStack The Upgrade Item Stack.
     * @return A map fo Upgrade Types.
     */
    Map<UpgradeTypes, Double> getUpgrades(ItemStack itemStack);

    /**
     * Return the main Upgrade type for the given Upgrade.
     * This is to check if an upgrade can even go in the upgrade slot.
     *
     * @param itemStack the upgrade stack.
     * @return the main upgrade type
     */
    UpgradeTypes getMainUpgrade(ItemStack itemStack);
}
