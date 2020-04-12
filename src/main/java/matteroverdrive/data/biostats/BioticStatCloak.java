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
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EnumSet;

public class BioticStatCloak extends AbstractBioticStat implements IConfigSubscriber {
    private static int ENERGY_PER_TICK = 128;

    public BioticStatCloak(String name, int xp) {
        super(name, xp);
        setShowOnHud(true);
        setShowOnWheel(true);
    }

    @Override
    public String getDetails(int level) {
        return String.format(super.getDetails(level), TextFormatting.YELLOW.toString() + ENERGY_PER_TICK + MOEnergyHelper.ENERGY_UNIT + TextFormatting.GRAY);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {
        if (!android.getPlayer().world.isRemote) {
            if (isActive(android, level)) {
                if (!android.getPlayer().isInvisible()) {
                    android.getPlayer().world.playSound(null, android.getPlayer().posX, android.getPlayer().posY, android.getPlayer().posZ, MatterOverdriveSounds.androidCloakOn, SoundCategory.PLAYERS, 1, 1);
                }
                android.getPlayer().setInvisible(true);
                android.extractEnergyScaled(ENERGY_PER_TICK);
            } else {
                if (android.getPlayer().isInvisible()) {
                    android.getPlayer().world.playSound(null, android.getPlayer().posX, android.getPlayer().posY, android.getPlayer().posZ, MatterOverdriveSounds.androidCloakOff, SoundCategory.PLAYERS, 1, 1);
                }
                android.getPlayer().setInvisible(false);
            }
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer android, int level, boolean server) {
        if (this.equals(android.getActiveStat()) && server) {
            setActive(android, level, !android.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_CLOAKED));
        }
    }

    private void setActive(AndroidPlayer android, int level, boolean active) {
        android.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_CLOAKED, active);
        android.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS), true);
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {
        if (!isEnabled(androidPlayer, level) && isActive(androidPlayer, level)) {
            setActive(androidPlayer, level, false);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_CLOAKED) && !androidPlayer.getPlayer().isHandActive();
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }

    @Override
    public boolean isEnabled(AndroidPlayer androidPlayer, int level) {
        return super.isEnabled(androidPlayer, level) && androidPlayer.hasEnoughEnergyScaled(ENERGY_PER_TICK);
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level) {
        return isActive(android, level);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        ENERGY_PER_TICK = config.getInt("cloak_energy_per_tick", ConfigurationHandler.CATEGORY_ABILITIES, 128, "The energy cost of the Cloak");
    }
}
