package com.hrznstudio.matteroverdrive.entity.projectile;

import com.hrznstudio.matteroverdrive.entity.MOEntities;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class KunaiEntity extends AbstractArrow {

    public KunaiEntity(EntityType<? extends KunaiEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public KunaiEntity(Level worldIn, LivingEntity shooter) {
        super(MOEntities.KUNAI.get(), shooter, worldIn);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return MOSounds.KUNAI_THUD.get();
    }

    @Override
    public void setSoundEvent(SoundEvent pSound) {
        super.setSoundEvent(this.getDefaultHitGroundSoundEvent());
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(MOItems.KUNAI.get());
    }
}
