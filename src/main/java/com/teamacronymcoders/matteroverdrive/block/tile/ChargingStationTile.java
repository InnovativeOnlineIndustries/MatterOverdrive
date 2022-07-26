package com.teamacronymcoders.matteroverdrive.block.tile;

import com.teamacronymcoders.matteroverdrive.block.ChargingStationBlock;
import com.teamacronymcoders.matteroverdrive.block.MOBlocks;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOPoweredMultiBlockTile;
import com.teamacronymcoders.matteroverdrive.block.extendable.tile.MOPoweredTile;
import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.capabilities.android.AndroidEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

public class ChargingStationTile extends MOPoweredMultiBlockTile<ChargingStationTile> {

    private boolean isDummy = true;

    public ChargingStationTile(BlockPos pos, BlockState state) {
        super(MOBlocks.CHARGING_STATION.get(), MOBlocks.CHARGING_STATION_TILE.get(), pos, state, Component.translatable("gui.matteroverdrive.title"));
        if (!state.getValue(ChargingStationBlock.TOP) && !state.getValue(ChargingStationBlock.MIDDLE)) {
            this.isDummy = false;
            this.setParent(true);
            this.setParentPos(pos);
            this.setChanged();
        }
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
        player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(android -> {
            if (android.isAndroid()) {
                openMenu(player);
            }
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, ChargingStationTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        if (!isDummy) {
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

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putBoolean("isDummy", this.isDummy);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        this.isDummy = compoundTag.getBoolean("isDummy");
        super.load(compoundTag);
    }
}
