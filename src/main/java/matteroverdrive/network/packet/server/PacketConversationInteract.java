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

package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConversationInteract extends PacketAbstract {
    int npcID;
    int dialogMessageID;
    int optionId;

    public PacketConversationInteract() {
    }

    public PacketConversationInteract(IDialogNpc npc, IDialogMessage dialogMessage, int optionId) {
        this.npcID = npc.getEntity().getEntityId();
        this.dialogMessageID = MatterOverdrive.DIALOG_REGISTRY.getMessageId(dialogMessage);
        this.optionId = optionId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npcID = buf.readInt();
        dialogMessageID = buf.readInt();
        optionId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(npcID);
        buf.writeInt(dialogMessageID);
        buf.writeInt(optionId);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketConversationInteract> {
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketConversationInteract message, MessageContext ctx) {
            Entity npcEntity = player.world.getEntityByID(message.npcID);
            if (npcEntity instanceof IDialogNpc) {
                IDialogMessage m;
                if (message.dialogMessageID >= 0) {
                    m = MatterOverdrive.DIALOG_REGISTRY.getMessage(message.dialogMessageID);
                } else {
                    m = ((IDialogNpc) npcEntity).getStartDialogMessage(player);
                }
                if (m != null) {
                    m.onOptionsInteract((IDialogNpc) npcEntity, player, message.optionId);
                }
            }
        }
    }
}
