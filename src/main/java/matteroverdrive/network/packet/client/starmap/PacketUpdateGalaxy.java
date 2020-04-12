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

package matteroverdrive.network.packet.client.starmap;

import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.util.MOLog;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateGalaxy extends PacketAbstract {

    Galaxy galaxy;

    public PacketUpdateGalaxy() {

    }

    public PacketUpdateGalaxy(Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        galaxy = new Galaxy();
        galaxy.readFromBuffer(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            galaxy.writeToBuffer(buf);
        } catch (Exception e) {
            MOLog.fatal("There was a problem writing the galaxy to buffer when sending to player", e);
        }
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdateGalaxy> {

        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketUpdateGalaxy message, MessageContext ctx) {
            message.galaxy.setWorld(player.world);
            GalaxyClient.getInstance().setTheGalaxy(message.galaxy);
            GalaxyClient.getInstance().loadClaimedPlanets();
        }
    }
}
