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

package matteroverdrive.api.android;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;

public interface IAndroid extends IInventory {
    String EXT_PROP_NAME = "AndroidPlayer";

    /**
     * Checks if the Player is an Android
     *
     * @return is the player an Android.
     */
    boolean isAndroid();

    /**
     * Checks if a given stat with a given level is unlocked by the Android Player.
     *
     * @param stat  the Bionic stat.
     * @param level the level of the bionic stat.
     * @return returns true if the given bionic stat at the given level is unlocked.
     */
    boolean isUnlocked(IBioticStat stat, int level);

    /**
     * Gets the unlocked level for a given biotic stat.
     * returns 0 if the bionic stat is not unlocked.
     *
     * @param stat the bionic stat.
     * @return returns the unlocked level of the given bionic stat. Returns 0 if the stat is not unlocked.
     */
    int getUnlockedLevel(IBioticStat stat);

    /**
     * Is the Android Player currently turning into an Android.
     * This is true while the transformation music and animation are playing.
     *
     * @return
     */
    boolean isTurning();

    /**
     * Gets the player.
     *
     * @return the player.
     */
    EntityPlayer getPlayer();

    /**
     * Gets the currently active/selected biotic stat.
     * The active stat is chosen by the player trough the ability wheel.
     * Return null if there is no active stat.
     *
     * @return the active/selected biotic stat.
     */
    IBioticStat getActiveStat();

    void onEffectsUpdate(int effectId);

    void writeToNBT(NBTTagCompound compound, EnumSet<DataType> dataTypes);

    void readFromNBT(NBTTagCompound compound, EnumSet<DataType> dataTypes);

    enum DataType {
        DATA, ENERGY, EFFECTS, STATS, ACTIVE_ABILITY, INVENTORY, BATTERY
    }
}
