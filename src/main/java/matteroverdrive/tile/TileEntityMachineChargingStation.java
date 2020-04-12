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
import matteroverdrive.api.machines.IUpgradeHandler;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMachineChargingStation extends MOTileEntityMachineEnergy implements IMultiBlockTileEntity {

    public static final int ENERGY_CAPACITY = 512000;
    public static final int ENERGY_TRANSFER = 512;
    private static final UpgradeHandler upgradeHandler = new UpgradeHandler();
    public static int BASE_MAX_RANGE = 8;

    public TileEntityMachineChargingStation() {
        super(2);
        energyStorage.setCapacity(ENERGY_CAPACITY);
        energyStorage.setMaxExtract(ENERGY_TRANSFER);
        energyStorage.setMaxReceive(ENERGY_TRANSFER);
        playerSlotsHotbar = true;
        playerSlotsMain = true;
    }

    @Override
    public void update() {
        super.update();
        manageAndroidCharging();
    }

    private void manageAndroidCharging() {
        if (!world.isRemote && getEnergyStorage().getEnergyStored() > 0) {
            int range = getRage();
            AxisAlignedBB radius = new AxisAlignedBB(getPos().add(-range, -range, -range), getPos().add(range, range, range));
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, radius);
            for (EntityPlayer player : players) {
                if (MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid()) {
                    int required = getRequiredEnergy(player, range);
                    int max = Math.min(getEnergyStorage().getEnergyStored(), getMaxCharging());
                    int toExtract = Math.min(required, max);
                    getEnergyStorage().extractEnergy(MOPlayerCapabilityProvider.GetAndroidCapability(player).receiveEnergy(toExtract, false), false);
                }
            }
        }
    }

    public int getRage() {
        return (int) (BASE_MAX_RANGE * getUpgradeMultiply(UpgradeTypes.Range));
    }

    public int getMaxCharging() {
        return (int) (ENERGY_TRANSFER / getUpgradeMultiply(UpgradeTypes.PowerUsage));
    }

    private int getRequiredEnergy(EntityPlayer player, int maxRange) {
        return (int) (ENERGY_TRANSFER * (1.0D - MathHelper.clamp((new Vec3d(player.posX, player.posY, player.posZ).subtract(new Vec3d(getPos())).length() / (double) maxRange), 0, 1)));
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
    public boolean getServerActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return type.equals(UpgradeTypes.Range) || type.equals(UpgradeTypes.PowerStorage) || type.equals(UpgradeTypes.PowerUsage);
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 8192.0D;
    }

    @Override
    public List<BlockPos> getBoundingBlocks() {
        List<BlockPos> s = new ArrayList<>();

        s.add(getPos().add(0, 1, 0));
        s.add(getPos().add(0, 2, 0));

        return s;
    }

    public IUpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    public static class UpgradeHandler implements IUpgradeHandler {

        @Override
        public double affectUpgrade(UpgradeTypes type, double multiply) {
            if (type.equals(UpgradeTypes.Range)) {
                return Math.min(8, multiply);
            }
            return multiply;
        }
    }
}
