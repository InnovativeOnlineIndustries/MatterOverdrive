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
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EnumSet;

public class BioticStatHighJump extends AbstractBioticStat implements IConfigSubscriber {

    private static int ENERGY_PER_JUMP = 1024;

    public BioticStatHighJump(String name, int xp) {
        super(name, xp);
        setMaxLevel(2);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {

    }

    @Override
    public String getDetails(int level) {
        return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.YELLOW.toString() + ENERGY_PER_JUMP + " FE" + TextFormatting.GRAY);
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {
        if (server && this.equals(androidPlayer.getActiveStat())) {
            androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_HIGH_JUMP, !androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_HIGH_JUMP));
            androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS));
        }
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {
        if (event instanceof LivingEvent.LivingJumpEvent && isActive(androidPlayer, level)) {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this, level, androidPlayer))) {
                if (!androidPlayer.getPlayer().isSneaking()) {
                    if (!event.getEntity().world.isRemote) {
                        androidPlayer.extractEnergyScaled(ENERGY_PER_JUMP * level);
                    }

                    Vec3d motion = new Vec3d(event.getEntityLiving().motionX, event.getEntityLiving().motionY, event.getEntityLiving().motionZ);
                    motion = motion.normalize().add(0, 1, 0).normalize();
                    event.getEntityLiving().addVelocity(motion.x * 0.25 * level, motion.y * 0.25 * level, motion.z * 0.25 * level);
                }
            }
        }
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android, level) && android.hasEnoughEnergyScaled(ENERGY_PER_JUMP) && android.getPlayer().onGround;
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level) {
        return isActive(android, level);
    }

    @Override
    public boolean showOnWheel(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getPlayer().isSneaking();
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_HIGH_JUMP);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        ENERGY_PER_JUMP = config.getInt("high_jump_energy", ConfigurationHandler.CATEGORY_ABILITIES, 1024, "The energy cost of each High Jump");
    }
}
