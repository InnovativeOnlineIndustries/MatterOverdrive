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
import matteroverdrive.blocks.BlockSpacetimeAccelerator;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.fx.ShockwaveParticle;
import matteroverdrive.machines.UpgradeHandlerGeneric;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.TimeTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

public class TileEntityMachineSpacetimeAccelerator extends MOTileEntityMachineMatter {
    private static final IUpgradeHandler upgradeHandler = new UpgradeHandlerGeneric(0.05, Double.MAX_VALUE).addUpgradeMinimum(UpgradeTypes.Speed, 0.2).addUpgradeMaximum(UpgradeTypes.Range, 6);
    private static EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.PowerStorage, UpgradeTypes.PowerUsage, UpgradeTypes.Range, UpgradeTypes.MatterStorage, UpgradeTypes.MatterTransfer, UpgradeTypes.Speed, UpgradeTypes.MatterUsage);
    private TimeTracker timeTracker;
    private double matterUseCache;

    public TileEntityMachineSpacetimeAccelerator() {
        super(4);
        timeTracker = new TimeTracker();
        matterStorage.setCapacity(1024);
        matterStorage.setMaxReceive(16);
        matterStorage.setMaxExtract(0);
        energyStorage.setCapacity(512000);
        energyStorage.setMaxExtract(0);
        energyStorage.setMaxReceive(512);
        playerSlotsHotbar = true;
        playerSlotsMain = true;
    }

    @Override
    public void update() {
        super.update();
        if (isActive()) {
            if (!world.isRemote) {
                energyStorage.modifyEnergyStored(-getEnergyUsage());
                UpdateClientPower();
                if (timeTracker.hasDelayPassed(world, getSpeed())) {
                    manageAccelerations();
                }
            } else {
                if (timeTracker.hasDelayPassed(world, Math.max(getSpeed(), 20))) {
                    boolean showWaveParticle = true;
                    if (getBlockType() instanceof BlockSpacetimeAccelerator) {
                        showWaveParticle = ((BlockSpacetimeAccelerator) getBlockType()).showWave;
                    }
                    if (showWaveParticle) {
                        spawnShockwave();
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnShockwave() {
        float range = getRadius();
        ShockwaveParticle particle = new ShockwaveParticle(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, range);
        particle.setColorRGBA(new Color(51, 78, 120));
        ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(particle, RenderParticlesHandler.Blending.Additive2Sided);
    }

    public void manageAccelerations() {
        int radius = getRadius();
        matterUseCache += getMatterUsage();
        if (matterUseCache > 1) {
            matterStorage.modifyMatterStored(-(int) matterUseCache);
            matterUseCache -= (int) matterUseCache;
        }
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                BlockPos pos = getPos().add(x, 0, z);
                IBlockState blockState = world.getBlockState(pos);
                blockState.getBlock().randomTick(world, pos, blockState, random);
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity != null && tileEntity instanceof ITickable && !(tileEntity instanceof TileEntityMachineSpacetimeAccelerator)) {
                    ((ITickable) tileEntity).update();
                }
            }
        }
    }

    public int getEnergyUsage() {
        return (int) (64 * getUpgradeMultiply(UpgradeTypes.PowerUsage));
    }

    public double getMatterUsage() {
        return 0.2 * getUpgradeMultiply(UpgradeTypes.MatterUsage);
    }

    public int getSpeed() {
        return (int) (40 * getUpgradeMultiply(UpgradeTypes.Speed));
    }

    public int getRadius() {
        return (int) (2 * getUpgradeMultiply(UpgradeTypes.Range));
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
        return getRedstoneActive() && matterStorage.getMatterStored() >= Math.max(1, getMatterUsage()) && energyStorage.getEnergyStored() >= getEnergyUsage();
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return upgradeTypes.contains(type);
    }

    @Override
    public IUpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }
}
