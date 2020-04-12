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

package matteroverdrive.handler.weapon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.weapon.MOEventEnergyWeapon;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class CommonWeaponHandler {
    private static final PacketFirePlasmaShot.BiHandler firePlasmaShotHandler = new PacketFirePlasmaShot.BiHandler();
    private final Map<EntityPlayer, Long> weaponTimestamps;

    public CommonWeaponHandler() {
        weaponTimestamps = new HashMap<>();
    }

    public void addTimestamp(EntityPlayer player, long timestamp) {
        weaponTimestamps.put(player, timestamp);
    }

    public boolean hasTimestamp(EntityPlayer player) {
        return weaponTimestamps.containsKey(player);
    }

    public long getTimestamp(EntityPlayer entityPlayer) {
        if (entityPlayer != null) {
            return weaponTimestamps.get(entityPlayer);
        }
        return 0;
    }

    public void handlePlasmaShotFire(EntityPlayer entityPlayer, PacketFirePlasmaShot plasmaShot, long timeStamp) {
        int delay = (int) (timeStamp - getTimestamp(entityPlayer));
        firePlasmaShotHandler.handleServerShot(entityPlayer, plasmaShot, delay);
        MatterOverdrive.NETWORK.sendToAllAround(plasmaShot, entityPlayer, plasmaShot.getShot().getRange() + 64);
    }

    @SubscribeEvent
    public void onEnergyWeaponEvent(MOEventEnergyWeapon eventEnergyWeapon) {
        if (eventEnergyWeapon.getEntity() != null) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(eventEnergyWeapon.getEntity());
            if (androidPlayer != null) {
                androidPlayer.onWeaponEvent(eventEnergyWeapon);
            }
        }
    }
}
