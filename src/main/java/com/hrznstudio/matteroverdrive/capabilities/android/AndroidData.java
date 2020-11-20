package com.hrznstudio.matteroverdrive.capabilities.android;

import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.server.AndroidSyncAllPacket;
import com.hrznstudio.matteroverdrive.network.server.AndroidTurningTimeSyncPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class AndroidData implements IAndroid {

    public static final int TURNING_TIME = 30 * 21;

    private static final String IS_ANDROID_NBT = "IsAndroid";
    private static final String TRANSFORMATION_TIME_NBT = "TransformationTime";

    private boolean isAndroid;
    private int transformationTime;
    private boolean needsUpdate;

    public AndroidData() {
        this.isAndroid = false;
        this.transformationTime = 0;
        this.needsUpdate = false;
    }

    public boolean isAndroid() {
        return isAndroid;
    }

    public void setAndroid(boolean android) {
        isAndroid = android;
    }

    @Override
    public boolean isTurning() {
        return transformationTime > 0 && !isAndroid;
    }

    public int getTurningTime() {
        return transformationTime;
    }

    public void setTurningTime(int turningTime) {
        this.transformationTime = turningTime;
    }

    @Override
    public void requestUpdate() {
        this.needsUpdate = true;
    }

    @Override
    public void tickClient(Entity entity) {

    }

    @Override
    public void tickServer(Entity entity) {
        if (isTurning()){
            tickTransformationTime(entity);
        }
        if (needsUpdate){
            sync(entity);
        }
    }

    public void sync(Entity entity){
        if (entity instanceof ServerPlayerEntity){
            PacketHandler.sendToPlayer(new AndroidSyncAllPacket(serializeNBT()), (ServerPlayerEntity) entity);
        }
    }

    private void tickTransformationTime(Entity entity){
        DamageSource fake = new DamageSource("android_transformation");
        fake.setDamageIsAbsolute();
        fake.setDamageBypassesArmor();
        --transformationTime;
        if (entity instanceof ServerPlayerEntity){
            PacketHandler.sendToPlayer(new AndroidTurningTimeSyncPacket(transformationTime), (ServerPlayerEntity) entity);
        }
        if (entity instanceof LivingEntity){
            if (transformationTime > 0) {
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20, 2, false, false));
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.HUNGER, 20, 0, false, false));
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WEAKNESS, 20, 0, false, false));
                if (transformationTime % 40 == 0) {
                    entity.attackEntityFrom(fake, 0.1f);
                }
            }
        }
        if (transformationTime <= 0) {
            setAndroid(true);
            requestUpdate();
            if (entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative() && !entity.world.getWorldInfo().isHardcore()) {
                entity.attackEntityFrom(fake, Integer.MAX_VALUE);
            }
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean(IS_ANDROID_NBT, this.isAndroid);
        nbt.putInt(TRANSFORMATION_TIME_NBT, this.transformationTime);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.isAndroid = nbt.getBoolean(IS_ANDROID_NBT);
        this.transformationTime = nbt.getInt(TRANSFORMATION_TIME_NBT);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == MOCapabilities.ANDROID_DATA ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }
}
