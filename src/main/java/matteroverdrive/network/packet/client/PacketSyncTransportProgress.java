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

package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncTransportProgress extends TileEntityUpdatePacket {
    int progress;

    public PacketSyncTransportProgress() {
    }

    public PacketSyncTransportProgress(TileEntityMachineTransporter transporter) {
        super(transporter);
        this.progress = transporter.getTransportTime();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        progress = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(progress);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncTransportProgress> {
        public ClientHandler() {
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSyncTransportProgress message, MessageContext ctx) {
            TileEntity entity = message.getTileEntity(player.world);
            if (entity instanceof TileEntityMachineTransporter) {
                ((TileEntityMachineTransporter) entity).setTransportTime(message.progress);
            }
        }
    }
}
