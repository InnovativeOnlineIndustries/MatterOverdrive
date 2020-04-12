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
import matteroverdrive.items.DataPad;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDataPadCommands extends PacketAbstract {
    public static final int COMMAND_ORDERING = 1;
    NBTTagCompound data;
    int command;
    EnumHand hand;

    public PacketDataPadCommands() {

    }

    public PacketDataPadCommands(EnumHand hand, ItemStack dataPad) {
        this(hand, dataPad, 0);
    }

    public PacketDataPadCommands(EnumHand hand, ItemStack dataPad, int command) {
        data = new NBTTagCompound();
        if (dataPad != null) {
            if (dataPad.hasTagCompound()) {
                if (command == 0) {
                    data = dataPad.getTagCompound();
                }
            }
        }
        this.hand = hand;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        command = buf.readInt();
        data = ByteBufUtils.readTag(buf);
        hand = buf.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(command);
        ByteBufUtils.writeTag(buf, data);
        buf.writeBoolean(hand == EnumHand.MAIN_HAND);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketDataPadCommands> {

        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketDataPadCommands message, MessageContext ctx) {
            ItemStack dataPadStack = player.getHeldItem(message.hand);
            if (dataPadStack != null && dataPadStack.getItem() instanceof DataPad) {
                if (message.command == 0) {
                    dataPadStack.setTagCompound(message.data);
                }
            }
        }
    }
}
