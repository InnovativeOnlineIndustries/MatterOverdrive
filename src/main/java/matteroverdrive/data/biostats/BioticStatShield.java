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

package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.entity.android_player.AndroidAttributes;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

public class BioticStatShield extends AbstractBioticStat implements IConfigSubscriber {
    private static final int SHIELD_COOLDOWN = 20 * 16;
    private static final int SHIELD_TIME = 20 * 8;
    private static int ENERGY_PER_TICK = 64;
    private static int ENERGY_PER_DAMAGE = 256;
    private final AttributeModifier modifyer;
    private final Random random;
    @SideOnly(Side.CLIENT)
    private MOPositionedSound shieldSound;

    public BioticStatShield(String name, int xp) {
        super(name, xp);
        setShowOnHud(true);
        modifyer = new AttributeModifier(UUID.fromString("ead117ad-105a-43fe-ab22-a31aee6adc42"), "Shield Slowdown", -0.4, 2);
        random = new Random();
        setShowOnWheel(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {
        if (!android.getPlayer().world.isRemote) {
            if (android.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD)) {
                android.extractEnergyScaled(ENERGY_PER_TICK);
            }

            /*if (android.hasEffect(AndroidPlayer.NBT_HITS)) {
				NBTTagList attackList = android.getEffectTagList(AndroidPlayer.NBT_HITS, Constants.NBT.TAG_COMPOUND);

                if (attackList.tagCount() > 0)
                {
                    if (attackList.getCompoundTagAt(0).getInteger("t") > 0)
                    {
                        attackList.getCompoundTagAt(0).setInteger("t",attackList.getCompoundTagAt(0).getInteger("t") - 1);
                    }
                    else
                    {
                        attackList.removeTag(0);
                    }
                } else {
                    android.removeEffect(AndroidPlayer.NBT_HITS);
                }

                android.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
            }*/
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {
        if (this.equals(androidPlayer.getActiveStat()) && canActivate(androidPlayer) && !MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this, level, androidPlayer))) {
            setShield(androidPlayer, true);
            androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS), true);
        }
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    void setShield(AndroidPlayer androidPlayer, boolean on) {
        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHIELD, on);
        setLastShieldTime(androidPlayer, androidPlayer.getPlayer().world.getTotalWorldTime() + SHIELD_COOLDOWN + SHIELD_TIME);
        androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS), true);
        androidPlayer.getPlayer().world.playSound(null, androidPlayer.getPlayer().posX, androidPlayer.getPlayer().posY, androidPlayer.getPlayer().posZ, MatterOverdriveSounds.androidShieldPowerUp, SoundCategory.PLAYERS, 0.6f + random.nextFloat() * 0.2f, 1);
        //androidPlayer.getPlayer().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).removeModifier(modifyer);
        //androidPlayer.getPlayer().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).applyModifier(modifyer);
    }

    public String getDetails(int level) {
        String key = "Unknown";
        try {
            key = Keyboard.getKeyName(ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).getKeyCode());
        } catch (Exception ignored) {

        }
        return MOStringHelper.translateToLocal(getUnlocalizedDetails(), key);
    }

    public boolean getShieldState(AndroidPlayer androidPlayer) {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD);
    }

    private long getLastShieldTime(AndroidPlayer androidPlayer) {
        return androidPlayer.getAndroidEffects().getEffectLong(AndroidPlayer.EFFECT_SHIELD_LAST_USE);
    }

    private void setLastShieldTime(AndroidPlayer androidPlayer, long time) {
        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHIELD_LAST_USE, time);
    }

    boolean canActivate(AndroidPlayer androidPlayer) {
        return getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().world.getTotalWorldTime() <= 0;
    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {
        if (event instanceof LivingAttackEvent) {
            DamageSource source = ((LivingAttackEvent) event).getSource();
            if (getShieldState(androidPlayer)) {
                int energyReqired = MathHelper.ceil(((LivingAttackEvent) event).getAmount() * ENERGY_PER_DAMAGE);

                if (isDamageValid(source) && event.isCancelable()) {
                    if (source.getTrueSource() != null) {

                        //NBTTagCompound attack = new NBTTagCompound();
                        //NBTTagList attackList = androidPlayer.getEffectTagList(AndroidPlayer.NBT_HITS, 10);
                        //attack.setDouble("x", source.getSourceOfDamage().posX - event.entityLiving.posX);
                        //attack.setDouble("y", source.getSourceOfDamage().posY - (event.entityLiving.posY + 1.5));
                        //attack.setDouble("z", source.getSourceOfDamage().posZ - event.entityLiving.posZ);
                        //attack.setInteger("time", 10);
                        //attackList.appendTag(attack);
                        //androidPlayer.setEffectsTag(AndroidPlayer.NBT_HITS, attackList);
                        //androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
                        androidPlayer.getPlayer().world.playSound(null, androidPlayer.getPlayer().posX, androidPlayer.getPlayer().posY, androidPlayer.getPlayer().posZ, MatterOverdriveSounds.androidShieldHit, SoundCategory.PLAYERS, 0.5f, 0.9f + random.nextFloat() * 0.2f);
                    }

                    if (androidPlayer.hasEnoughEnergyScaled(energyReqired)) {
                        androidPlayer.extractEnergyScaled(energyReqired);
                        event.setCanceled(true);
                    }
                }
            }
        } else if (event instanceof LivingHurtEvent) {
            DamageSource source = ((LivingHurtEvent) event).getSource();
            if (getShieldState(androidPlayer)) {
                int energyReqired = MathHelper.ceil(((LivingHurtEvent) event).getAmount() * ENERGY_PER_DAMAGE);

                if (isDamageValid(source)) {
                    double energyMultiply = androidPlayer.getPlayer().getAttributeMap().getAttributeInstance(AndroidAttributes.attributeBatteryUse).getAttributeValue();
                    energyReqired *= energyMultiply;
                    int energyExtracted = androidPlayer.extractEnergy(energyReqired, true);
                    ((LivingHurtEvent) event).setAmount(((LivingHurtEvent) event).getAmount() + (float) energyExtracted / (float) energyReqired);
                }
            }
        }
    }

    boolean isDamageValid(DamageSource damageSource) {
        return damageSource.isExplosion() || damageSource.isProjectile();
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {
        if (androidPlayer.getPlayer().world.isRemote) {
            if (!androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD)) {
                stopShieldSound();
            } else {
                playShieldSound();
            }
        } else {
            long shieldTime = getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().world.getTotalWorldTime();
            if (shieldTime < SHIELD_COOLDOWN && androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_SHIELD)) {
                androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_SHIELD, false);
                //androidPlayer.removeEffect(AndroidPlayer.NBT_HITS);
                //androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS),true);
                androidPlayer.getPlayer().world.playSound(null, androidPlayer.getPlayer().posX, androidPlayer.getPlayer().posY, androidPlayer.getPlayer().posZ, MatterOverdriveSounds.androidShieldPowerDown, SoundCategory.PLAYERS, 0.6f + random.nextFloat() * 0.2f, 1);
                //androidPlayer.init(androidPlayer.getPlayer(),androidPlayer.getPlayer().world);
            }
        }
    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        //Multimap multimap = HashMultimap.create();
        //multimap.put(SharedMonsterAttributes.movementSpeed.getName(),modifyer);
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void playShieldSound() {
        if (shieldSound == null && !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(shieldSound)) {
            shieldSound = new MOPositionedSound(MatterOverdriveSounds.androidShieldLoop, SoundCategory.PLAYERS, 0.3f + random.nextFloat() * 0.2f, 1);
            shieldSound.setRepeat(true);
            Minecraft.getMinecraft().getSoundHandler().playSound(shieldSound);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopShieldSound() {
        if (shieldSound != null && Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(shieldSound)) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(shieldSound);
            shieldSound = null;
        }
    }

    @Override
    public boolean isEnabled(AndroidPlayer androidPlayer, int level) {
        long shieldTime = getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().world.getTotalWorldTime();
        return super.isEnabled(androidPlayer, level) && androidPlayer.hasEnoughEnergyScaled(ENERGY_PER_TICK) && (shieldTime <= 0 || shieldTime > SHIELD_COOLDOWN);
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return getShieldState(androidPlayer);
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level) {
        return this.equals(android.getActiveStat()) || getShieldState(android);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        long shieldTime = getLastShieldTime(androidPlayer) - androidPlayer.getPlayer().world.getTotalWorldTime();
        if (shieldTime > 0) {
            return (int) shieldTime;
        }
        return 0;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        ENERGY_PER_DAMAGE = config.getInt("shield_energy_per_damage", ConfigurationHandler.CATEGORY_ABILITIES, 256, "The energy cost of each hit to the shield");
        ENERGY_PER_TICK = config.getInt("shield_energy_per_tick", ConfigurationHandler.CATEGORY_ABILITIES, 64, "The energy cost of the shield per tick");
    }
}
