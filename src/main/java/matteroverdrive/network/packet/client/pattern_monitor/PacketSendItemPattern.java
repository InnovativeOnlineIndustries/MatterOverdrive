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

package matteroverdrive.network.packet.client.pattern_monitor;

import io.netty.buffer.ByteBuf;
import matteroverdrive.container.matter_network.ContainerPatternMonitor;
import matteroverdrive.data.matter_network.ItemPatternMapping;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendItemPattern extends PacketAbstract {
    ItemPatternMapping itemPatternMapping;

    public PacketSendItemPattern() {
        super();
    }

    public PacketSendItemPattern(int containerId, ItemPatternMapping itemPatternMapping) {
        this.itemPatternMapping = itemPatternMapping;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        itemPatternMapping = new ItemPatternMapping(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        itemPatternMapping.writeToBuffer(buf);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSendItemPattern> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSendItemPattern message, MessageContext ctx) {
            if (player.openContainer instanceof ContainerPatternMonitor) {
                ((ContainerPatternMonitor) player.openContainer).setItemPattern(message.itemPatternMapping);
            }
        }
    }
}
