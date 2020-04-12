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

package matteroverdrive.api.matter;

import matteroverdrive.data.matter.IMatterEntryHandler;

/**
 * Created by Simeon on 7/21/2015.
 * All matter values for items stored in instances of this class.
 * Used in the {@link IMatterRegistry} to store matter values on items.
 */
public interface IMatterEntry<KEY, MAT> {
    /**
     * The amount of matter the entry is composed of.
     *
     * @return The matter amount of the entry.
     */
    int getMatter(MAT obj);

    void addHandler(IMatterEntryHandler<MAT> handler);
}
