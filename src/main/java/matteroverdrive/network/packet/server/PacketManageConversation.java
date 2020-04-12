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
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.gui.GuiDialog;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketManageConversation extends PacketAbstract {
    public boolean start;
    public int npcID;

    public PacketManageConversation() {

    }

    public PacketManageConversation(IDialogNpc npc, boolean start) {
        npcID = npc.getEntity().getEntityId();
        this.start = start;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        npcID = buf.readInt();
        start = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(npcID);
        buf.writeBoolean(start);
    }

    public static class BiHandler extends AbstractBiPacketHandler<PacketManageConversation> {
        @Override
        @SideOnly(Side.CLIENT)
        public void handleClientMessage(EntityPlayerSP player, PacketManageConversation message, MessageContext ctx) {
            Entity npcEntity = player.world.getEntityByID(message.npcID);
            if (npcEntity instanceof IDialogNpc) {
                if (message.start) {
                    ((IDialogNpc) npcEntity).onPlayerInteract(player, null);
                    ((IDialogNpc) npcEntity).setDialogPlayer(player);
                    Minecraft.getMinecraft().displayGuiScreen(new GuiDialog((IDialogNpc) npcEntity, player));
                } else {
                    ((IDialogNpc) npcEntity).setDialogPlayer(null);
                }
            }
        }

        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketManageConversation message, MessageContext ctx) {
            Entity npcEntity = player.world.getEntityByID(message.npcID);
            if (npcEntity instanceof IDialogNpc) {
                if (message.start) {
                    if (((IDialogNpc) npcEntity).getDialogPlayer() == null && ((IDialogNpc) npcEntity).canTalkTo(player)) {
                        ((IDialogNpc) npcEntity).setDialogPlayer(player);
                        MatterOverdrive.NETWORK.sendTo(message, player);
                    }
                } else {
                    ((IDialogNpc) npcEntity).setDialogPlayer(null);
                    MatterOverdrive.NETWORK.sendTo(message, player);
                }
            }
        }
    }
}
