package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.capabilities.AndroidEnergyCapability;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;

public class ChargingStationTile extends PoweredTile<ChargingStationTile> {

    public ChargingStationTile() {
        super(MOBlocks.CHARGING_STATION.get());
    }

    @Nonnull
    @Override
    public ChargingStationTile getSelf() {
        return this;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ) == ActionResultType.SUCCESS) {
            return ActionResultType.SUCCESS;
        }
        openGui(playerIn);
        return ActionResultType.SUCCESS;
    }


    @Override
    public void tick() {
        super.tick();
        if (isServer()) {
            this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPos()).grow(4), entity -> true).forEach(entity -> {
                entity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                    this.getEnergyStorage().extractEnergy(energyStorage.receiveEnergy(this.getEnergyStorage().extractEnergy(512, true), false), false);
                    if (entity instanceof ServerPlayerEntity)
                        AndroidEnergyCapability.syncEnergy((ServerPlayerEntity) entity);
                });
            });
        }
    }
}
