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
import matteroverdrive.api.events.weapon.MOEventEnergyWeapon;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.client.render.HoloIcons;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.text.DecimalFormat;
import java.util.Random;

public class BioticStatFlashCooling extends AbstractBioticStat {
    private static final float COOLDOWN_CHANGE = 0.2f;
    private static final int ENERGY_PER_COOLDOWN = 1024;
    private final Random random;

    public BioticStatFlashCooling(String name, int xp) {
        super(name, xp);
        random = new Random();
    }

    @Override
    public String getDetails(int level) {
        return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.GREEN + DecimalFormat.getPercentInstance().format(COOLDOWN_CHANGE) + TextFormatting.GRAY, TextFormatting.YELLOW + (ENERGY_PER_COOLDOWN + MOEnergyHelper.ENERGY_UNIT) + TextFormatting.GRAY);
    }

    @Override
    public void registerIcons(TextureMap textureMap, HoloIcons holoIcons) {
        this.icon = holoIcons.getIcon("temperature");
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {

    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {
        if (event instanceof MOEventEnergyWeapon.Overheat && random.nextFloat() < COOLDOWN_CHANGE) {
            event.setCanceled(true);
            ((MOEventEnergyWeapon.Overheat) event).energyWeapon.setHeat(((MOEventEnergyWeapon.Overheat) event).weaponStack, 0);
            event.getEntity().world.playSound(null, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, MatterOverdriveSounds.weaponsOverheat, SoundCategory.PLAYERS, 1F, 1f);
        }
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android, level) && android.getEnergyStored() > 0;
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return false;
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level) {
        return android.getPlayer().getHeldItem(EnumHand.MAIN_HAND) != null && android.getPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IWeapon;
    }
}
