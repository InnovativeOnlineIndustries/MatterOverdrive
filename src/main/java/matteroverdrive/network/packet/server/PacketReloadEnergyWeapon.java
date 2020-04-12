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
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloadEnergyWeapon extends PacketAbstract {
    public PacketReloadEnergyWeapon() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketReloadEnergyWeapon> {
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketReloadEnergyWeapon message, MessageContext ctx) {
            if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof EnergyWeapon) {
                if (((EnergyWeapon) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).needsRecharge(player.getHeldItem(EnumHand.MAIN_HAND))) {
                    ((EnergyWeapon) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).chargeFromEnergyPack(player.getHeldItem(EnumHand.MAIN_HAND), player);
                }
            }
        }
    }
}