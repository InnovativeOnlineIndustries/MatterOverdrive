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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import matteroverdrive.data.MOAttributeModifier;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.UUID;

public class BiostatNanobots extends AbstractBioticStat implements IConfigSubscriber {
    private final static float REGEN_AMOUNT_PER_TICK = 0.05f;
    private static int ENERGY_PER_REGEN = 32;
    private final UUID modifierID = UUID.fromString("4548003d-1566-49aa-9378-8be2f9a064ab");

    public BiostatNanobots(String name, int xp) {
        super(name, xp);
        setShowOnHud(true);
        setMaxLevel(4);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {
        if (android.getPlayer().world.getWorldTime() % 20 == 0) {
            if (android.getPlayer().getHealth() > 0 && !android.getPlayer().isDead && android.getPlayer().getHealth() < android.getPlayer().getMaxHealth() && android.hasEnoughEnergyScaled(ENERGY_PER_REGEN)) {
                android.getPlayer().heal(REGEN_AMOUNT_PER_TICK * 20);
                android.extractEnergyScaled(ENERGY_PER_REGEN * 20);
            }
        }

        //android.getPlayer().stepHeight = 1;
    }

    @Override
    public String getDetails(int level) {
        return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.GREEN.toString() + (REGEN_AMOUNT_PER_TICK * 20), TextFormatting.GREEN.toString() + "+" + getHealthBoost(level));
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new MOAttributeModifier(modifierID, "Android Health", getHealthBoost(level), 0).setSaved(false));
        return multimap;
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android, level) && android.getEnergyStored() > 0;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return isEnabled(androidPlayer, level) && androidPlayer.getPlayer().getHealth() < androidPlayer.getPlayer().getMaxHealth();
    }

    public int getHealthBoost(int level) {
        return level * 5;
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        ENERGY_PER_REGEN = config.getInt("heal_energy_per_regen", ConfigurationHandler.CATEGORY_ABILITIES, 32, "The energy cost of each heal by the Nanobots ability");
    }
}
