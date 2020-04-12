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

package matteroverdrive.network.packet.bi;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.server.AbstractServerPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWeaponTick extends PacketAbstract {

    long timestamp;
    PacketFirePlasmaShot plasmaShot;

    public PacketWeaponTick() {

    }

    public PacketWeaponTick(long timestamp) {
        this.timestamp = timestamp;
    }

    public PacketWeaponTick(long timestamp, PacketFirePlasmaShot plasmaShot) {
        this(timestamp);
        this.plasmaShot = plasmaShot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        timestamp = buf.readLong();
        if (buf.readBoolean()) {
            this.plasmaShot = new PacketFirePlasmaShot();
            this.plasmaShot.fromBytes(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(timestamp);
        buf.writeBoolean(plasmaShot != null);
        if (plasmaShot != null) {
            plasmaShot.toBytes(buf);
        }
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketWeaponTick> {
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketWeaponTick message, MessageContext ctx) {
            if (message.plasmaShot != null) {
                MatterOverdrive.PROXY.getWeaponHandler().handlePlasmaShotFire(player, message.plasmaShot, message.timestamp);
            }
            MatterOverdrive.PROXY.getWeaponHandler().addTimestamp(player, message.timestamp);
        }
    }
}
