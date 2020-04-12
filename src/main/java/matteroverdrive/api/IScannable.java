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

package matteroverdrive.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 * Used for scannable blocks or tile entities.
 */
public interface IScannable {
    /**
     * Used to add info to the Holo Screen.
     *
     * @param world the world.
     * @param x     the X inate of the scannable target.
     * @param y     the Y inate of the scannable target.
     * @param z     the Z inate of the scannable target.
     * @param infos the info lines list.
     *              Here is where you add the info lines you want displayed.
     */
    void addInfo(World world, double x, double y, double z, List<String> infos);

    /**
     * Called when the target is scanned.
     * Once the scanning process is complete.
     *
     * @param world   the world.
     * @param x       the X inates of the scanned target.
     * @param y       the Y inates of the scanned target.
     * @param z       the Z inates of the scanned target.
     * @param player  the player who scanned the target.
     * @param scanner the scanner item stack.
     */
    void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner);
}
