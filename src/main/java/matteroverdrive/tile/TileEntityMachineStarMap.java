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

package matteroverdrive.tile;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.GalaxyServer;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.starmap.data.Star;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

public class TileEntityMachineStarMap extends MOTileEntityMachineEnergy {
    GalacticPosition position;
    GalacticPosition destination;
    int zoomLevel;

    public TileEntityMachineStarMap() {
        super(0);
        position = new GalacticPosition();
        destination = new GalacticPosition();
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        super.RegisterSlots(inventory);
    }

    @Override
    public boolean getServerActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (getInventory() != inventory) {
            getInventory().markDirty();
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setByte("ZoomLevel", (byte) zoomLevel);
            NBTTagCompound positionTag = new NBTTagCompound();
            NBTTagCompound destinationTag = new NBTTagCompound();
            position.writeToNBT(positionTag);
            destination.writeToNBT(destinationTag);
            nbt.setTag("GalacticPosition", positionTag);
            nbt.setTag("GalacticDestination", destinationTag);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            zoomLevel = nbt.getByte("ZoomLevel");
            GalacticPosition newPosition = new GalacticPosition(nbt.getCompoundTag("GalacticPosition"));
            GalacticPosition newDestination = new GalacticPosition(nbt.getCompoundTag("GalacticDestination"));
            position = newPosition;
            destination = newDestination;
        }
    }

    public void zoom() {
        if (getZoomLevel() < getMaxZoom()) {
            setZoomLevel(getZoomLevel() + 1);
        } else {
            setZoomLevel(0);
        }
        forceSync();
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().getX() - 3, getPos().getY(), getPos().getZ() - 3, getPos().getX() + 3, getPos().getY() + 5, getPos().getZ() + 3);
    }

    @Override
    public IInventory getInventory() {
        if (getPlanet() != null) {
            return getPlanet();
        } else {
            return inventory;
        }
    }

    public Planet getPlanet() {
        if (world.isRemote) {
            return GalaxyClient.getInstance().getPlanet(destination);
        } else {
            return GalaxyServer.getInstance().getPlanet(destination);
        }
    }

    public Star getStar() {
        if (world.isRemote) {
            return GalaxyClient.getInstance().getStar(destination);
        } else {
            return GalaxyServer.getInstance().getStar(destination);
        }
    }

    public Quadrant getQuadrant() {
        if (world.isRemote) {
            return GalaxyClient.getInstance().getQuadrant(destination);
        } else {
            return GalaxyServer.getInstance().getQuadrant(destination);
        }
    }

    public int getMaxZoom() {
        if (getPlanet() != null) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
        if (event instanceof MachineEvent.Placed) {
            MachineEvent.Placed placed = (MachineEvent.Placed) event;
            if (placed.entityLiving instanceof EntityPlayer) {
                if (placed.world.isRemote) {
                    Planet homeworld = GalaxyClient.getInstance().getHomeworld((EntityPlayer) placed.entityLiving);
                    if (homeworld != null) {
                        position = new GalacticPosition(homeworld);
                    }
                } else {
                    Planet homeworld = GalaxyServer.getInstance().getHomeworld((EntityPlayer) placed.entityLiving);
                    if (homeworld != null) {
                        position = new GalacticPosition(homeworld);
                    }
                }

                destination = new GalacticPosition(position);
                owner = ((EntityPlayer) placed.entityLiving).getGameProfile().getId();
            }
        }
    }

    public GalacticPosition getGalaxyPosition() {
        return position;
    }

    public void setGalaxticPosition(GalacticPosition position) {
        this.position = position;
    }

    public GalacticPosition getDestination() {
        return this.destination;
    }

    public void setDestination(GalacticPosition position) {
        this.destination = position;
    }

    public SpaceBody getActiveSpaceBody() {
        switch (getZoomLevel()) {
            case 0:
                return GalaxyClient.getInstance().getTheGalaxy();
            case 1:
                return GalaxyClient.getInstance().getQuadrant(destination);
            case 2:
                return GalaxyClient.getInstance().getStar(destination);
            default:
                return GalaxyClient.getInstance().getPlanet(destination);
        }
    }

    public boolean isItemValidForSlot(int slot, ItemStack item, EntityPlayer player) {
        return (getPlanet() == null || getPlanet().isOwner(player)) && getInventory().isItemValidForSlot(slot, item);
    }

    public void onItemPickup(EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
        }
    }

    public void onItemPlaced(ItemStack itemStack) {
        if (!world.isRemote) {
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

}
