package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.block.extendable.tile.MOPoweredTile;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;

public class ChargingStationTile extends MOPoweredTile<ChargingStationTile> {

    public ChargingStationTile(BlockPos pos, BlockState state) {
        super(MOBlocks.CHARGING_STATION.get(), MOBlocks.CHARGING_STATION_TILE.get(), pos, state, Component.translatable("gui.matteroverdrive.title"));
    }

    @Nonnull
    @Override
    public ChargingStationTile getSelf() {
        return this;
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction hitDirection, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, hitDirection, hitX, hitY, hitZ) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        openGui(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, ChargingStationTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        if (isServer()) {
            this.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(this.getBlockPos()).inflate(4), entity -> true).forEach(entity -> {
                entity.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
                    this.getEnergyStorage().extractEnergy(storage.receiveEnergy(this.getEnergyStorage().extractEnergy(512, true), false), false);
                    if (entity instanceof ServerPlayer serverPlayer) {
                        AndroidEnergy.syncEnergy(serverPlayer);
                    }
                });
            });
        }
    }

}
