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

package matteroverdrive.api.transport;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by Simeon on 5/5/2015.
 * Stores inates and the name of transport locations.
 * Used by the transporter.
 */
public class TransportLocation {
    /**
     * The X,Y,Z inates of the location.
     */
    public BlockPos pos;
    /**
     * The name of the location.
     */
    public String name;


    public TransportLocation(BlockPos pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    public TransportLocation(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    public TransportLocation(NBTTagCompound nbt) {
        if (nbt != null) {
            pos = BlockPos.fromLong(nbt.getLong("tl"));
            name = nbt.getString("tl_name");
        }
    }


    public void writeToBuffer(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setLong("tl", pos.toLong());
        nbtTagCompound.setString("tl_name", name);
    }


    /**
     * Sets the name of the transport location.
     *
     * @param name the new transport location name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the transport location inates.
     */
    public void setPosition(BlockPos pos) {
        this.pos = pos;
    }

    /**
     * Calculates and returns the distance between this location and the given inates.
     *
     * @param pos the given position.
     * @return the distance between this transport location and the provided inates.
     */
    public int getDistance(BlockPos pos) {
        return (int) Math.sqrt(pos.distanceSq(this.pos));
    }
}
