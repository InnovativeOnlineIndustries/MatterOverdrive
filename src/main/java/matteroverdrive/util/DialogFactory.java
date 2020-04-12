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

package matteroverdrive.util;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.client.render.conversation.DialogShot;
import matteroverdrive.data.dialog.DialogMessage;
import net.minecraft.entity.player.EntityPlayer;

public class DialogFactory {
    private final IDialogRegistry registry;

    public DialogFactory(IDialogRegistry registry) {
        this.registry = registry;
    }

    public DialogMessage[] constructMultipleLineDialog(Class<? extends DialogMessage> mainMessageType, String unlocalizedName, int lines, String nextLineQuestion) {

        DialogMessage[] messages = new DialogMessage[lines];
        try {
            messages[0] = mainMessageType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            messages[0] = new DialogMessage();
        } finally {
            registry.registerMessage(messages[0]);
        }
        messages[0].setMessages(new String[]{String.format("%s.%s.line", unlocalizedName, 0)});
        messages[0].setQuestions(new String[]{unlocalizedName + ".question"});
        messages[0].setUnlocalized(true);

        DialogMessage lastChild = messages[0];
        for (int i = 1; i < lines; i++) {
            DialogMessage child = new DialogMessage("", nextLineQuestion);
            registry.registerMessage(child);
            child.setMessages(new String[]{String.format("%s.%s.line", unlocalizedName, i)});
            if (MOStringHelper.hasTranslation(String.format("%s.%s.question", unlocalizedName, i))) {
                child.setQuestions(new String[]{String.format("%s.%s.question", unlocalizedName, i)});
            }
            child.setUnlocalized(true);
            child.setParent(lastChild);
            lastChild.addOption(child);
            lastChild = child;
            messages[i] = child;
        }

        return messages;
    }

    public DialogMessage addOnlyVisibleOptions(EntityPlayer entityPlayer, IDialogNpc dialogNpc, DialogMessage parent, DialogMessage... options) {
        for (DialogMessage option : options) {
            if (option.isVisible(dialogNpc, entityPlayer)) {
                parent.addOption(option);
            }
        }
        return parent;
    }

    public void addRandomShots(DialogMessage dialogMessage) {
        dialogMessage.setShots(DialogShot.closeUp, DialogShot.dramaticCloseUp, DialogShot.wideNormal, DialogShot.wideOpposite, DialogShot.fromBehindLeftClose, DialogShot.fromBehindLeftFar, DialogShot.fromBehindRightClose, DialogShot.fromBehindRightFar);
    }
}
