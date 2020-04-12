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

package matteroverdrive.data.dialog;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DialogMessageAndroidTransformation extends DialogMessage {
    public DialogMessageAndroidTransformation() {
        super();
    }

    public DialogMessageAndroidTransformation(String message, String question) {
        super(message, question);
    }

    public DialogMessageAndroidTransformation(String message) {
        super(message);
    }

    @Override
    public boolean canInteract(IDialogNpc npc, EntityPlayer player) {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdrive.ITEMS.androidParts) {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length) {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (boolean hasPart : hasParts) {
            if (!hasPart) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onInteract(IDialogNpc npc, EntityPlayer player) {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdrive.ITEMS.androidParts) {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length) {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (boolean hasPart : hasParts) {
            if (!hasPart) {
                if (!player.world.isRemote) {
                    TextComponentString componentText = new TextComponentString(TextFormatting.GOLD + "<Mad Scientist>" + TextFormatting.RED + MOStringHelper.translateToLocal("entity.mad_scientist.line.fail." + player.getRNG().nextInt(4)));
                    componentText.setStyle(new Style().setColor(TextFormatting.RED));
                    player.sendMessage(componentText);
                }
                return;
            }
        }

        if (!player.world.isRemote) {
            for (int slot : slots) {
                player.inventory.decrStackSize(slot, 1);
            }
        }

        MOPlayerCapabilityProvider.GetAndroidCapability(player).startConversion();
        player.closeScreen();
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        return MOPlayerCapabilityProvider.GetAndroidCapability(player) == null || !MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid();
    }
}
