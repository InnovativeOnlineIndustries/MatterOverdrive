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

package matteroverdrive.api.dialog;

import net.minecraft.entity.player.EntityPlayer;

public interface IDialogOption {
    /**
     * Called when the option is chosen from all the option of the patten from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     * Not to be confused with {@link matteroverdrive.api.dialog.IDialogMessage#onOptionsInteract(IDialogNpc, EntityPlayer, int)} which is called on the parent.
     * This method is called after {@link matteroverdrive.api.dialog.IDialogMessage#onOptionsInteract(IDialogNpc, EntityPlayer, int)}.
     *
     * @param npc
     * @param player
     */
    void onInteract(IDialogNpc npc, EntityPlayer player);

    /**
     * Used to display the question (option) message.
     * This is used when the parent message is active and shows all children from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     *
     * @param npc    The NPC Entity.
     * @param player The Player
     * @return The question (option) text.
     */
    String getQuestionText(IDialogNpc npc, EntityPlayer player);

    /**
     * Can the player interact with this option. For messages means if it can be chosen as the next active message from the parent's options.
     *
     * @param npc    The NPC Entity
     * @param player The Player
     * @return Can the message be clicked (chosen) as an option.
     */
    boolean canInteract(IDialogNpc npc, EntityPlayer player);

    /**
     * Is the option visible.
     *
     * @param npc    The NPC entity.
     * @param player The Player.
     * @return Is the message visible as an option.
     */
    boolean isVisible(IDialogNpc npc, EntityPlayer player);

    /**
     * @param npc    the npc
     * @param player the player
     * @return The holo icon, {@code null} if there isn't one
     */
    String getHoloIcon(IDialogNpc npc, EntityPlayer player);

    boolean equalsOption(IDialogOption other);
}
