package com.hrznstudio.matteroverdrive.entity.projectile;

import com.hrznstudio.matteroverdrive.entity.MOEntities;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class KunaiEntity extends AbstractArrowEntity {

    public KunaiEntity(EntityType<? extends KunaiEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public KunaiEntity(World worldIn, LivingEntity shooter) {
        super(MOEntities.KUNAI.get(), shooter, worldIn);
    }

    @Override
    protected SoundEvent getHitEntitySound() {
        return MOSounds.KUNAI_THUD.get();
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(MOItems.KUNAI.get());
    }
}
