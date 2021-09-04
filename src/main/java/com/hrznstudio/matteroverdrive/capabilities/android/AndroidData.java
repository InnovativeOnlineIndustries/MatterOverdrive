package com.hrznstudio.matteroverdrive.capabilities.android;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import com.hrznstudio.matteroverdrive.api.android.perk.AndroidPerkManager;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.capabilities.AndroidEnergyCapability;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.server.AndroidSyncAllPacket;
import com.hrznstudio.matteroverdrive.network.server.AndroidTurningTimeSyncPacket;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.Random;

public class AndroidData implements IAndroid {

    public static final int TURNING_TIME = 30 * 21;

    private static final String IS_ANDROID_NBT = "isAndroid";
    private static final String TRANSFORMATION_TIME_NBT = "transformationTime";

    private boolean isAndroid;
    private int transformationTime;
    private boolean needsUpdate;
    private LivingEntity holder;
    private AndroidPerkManager perkManager;

    public AndroidData() {
        this.isAndroid = false;
        this.transformationTime = 0;
        this.needsUpdate = false;
        this.perkManager = new AndroidPerkManager();
    }

    public boolean isAndroid() {
        return this.isAndroid;
    }

    public void setAndroid(boolean android) {
        this.isAndroid = android;
    }

    @Override
    public boolean isTurning() {
        return this.transformationTime > 0 && !this.isAndroid;
    }

    public int getTurningTime() {
        return this.transformationTime;
    }

    public void setTurningTime(int turningTime) {
        this.transformationTime = turningTime;
    }

    @Override
    public void requestUpdate() {
        this.needsUpdate = true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickClient(Entity entity) {
        if (entity instanceof LivingEntity){
            this.holder = (LivingEntity) entity;
        }
        if (isTurning() && transformationTime % 40 == 0) {
            playGlitchSound(entity, entity.world.rand, 0.2f);
        }
    }

    private void playGlitchSound(Entity player, Random random, float amount) {
        player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), MOSounds.GLITCH.get(), SoundCategory.PLAYERS, amount, 0.9f + random.nextFloat() * 0.2f, false);
    }

    @Override
    public void tickServer(Entity entity) {
        if (entity instanceof LivingEntity){
            this.holder = (LivingEntity) entity;
        }
        tickPerks();
        if (entity instanceof ServerPlayerEntity){
            updatePerkAttributes((ServerPlayerEntity) entity);
        }
        if (isTurning()){
            tickTransformationTime(entity);
        }
        if (needsUpdate){
            sync(entity);
        }
    }

    @Override
    public AndroidPerkManager getPerkManager() {
        return perkManager;
    }

    @Override
    public LivingEntity getHolder() {
        return holder;
    }

    public void sync(Entity entity){
        if (entity instanceof ServerPlayerEntity){
            PacketHandler.sendToPlayer(new AndroidSyncAllPacket(serializeNBT()), (ServerPlayerEntity) entity);
            this.needsUpdate = false;
        }
    }

    public void updatePerkAttributes(ServerPlayerEntity player){
        for (String perk : this.getPerkManager().getOwned().keySet()) {
            if (IAndroidPerk.PERKS.containsKey(perk)){
                IAndroidPerk androidPerk = IAndroidPerk.PERKS.get(perk);
                if (!androidPerk.canBeToggled() || this.getPerkManager().hasPerkEnabled(androidPerk)){
                    player.getAttributeManager().reapplyModifiers(androidPerk.getAttributeModifiers(this, this.getPerkManager().getLevel(androidPerk)));
                }
            }
        }
    }

    public void tickPerks(){
        for (String perk : this.getPerkManager().getOwned().keySet()) {
            if (IAndroidPerk.PERKS.containsKey(perk)){
                IAndroidPerk androidPerk = IAndroidPerk.PERKS.get(perk);
                if (!androidPerk.canBeToggled() || this.getPerkManager().hasPerkEnabled(androidPerk)){
                    androidPerk.onAndroidTick(this, this.getPerkManager().getLevel(androidPerk));
                }
            }
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
            entity.getCapability(CapabilityEnergy.ENERGY).ifPresent(iEnergyStorage -> iEnergyStorage.receiveEnergy((int) (AndroidEnergyCapability.DEFAULT_ENERGY * 0.25), false));
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
        nbt.put("PerkManager", perkManager.serializeNBT());
        System.out.println(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.isAndroid = nbt.getBoolean(IS_ANDROID_NBT);
        this.transformationTime = nbt.getInt(TRANSFORMATION_TIME_NBT);
        this.perkManager.deserializeNBT(nbt.getCompound("PerkManager"));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == MOCapabilities.ANDROID_DATA ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }
}
