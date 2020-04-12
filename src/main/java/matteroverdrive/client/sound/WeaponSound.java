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

package matteroverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class WeaponSound extends PositionedSound implements ITickableSound {
    private boolean donePlaying;

    public WeaponSound(SoundEvent sound, SoundCategory category, float x, float y, float z, float volume, float pitch) {
        super(sound, category);
        setPosition(x, y, z);
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    public void stopPlaying() {
        donePlaying = true;
    }

    public void startPlaying() {
        donePlaying = false;
    }

    public void setPosition(float x, float y, float z) {
        this.xPosF = x;
        this.yPosF = y;
        this.zPosF = z;
    }

    @Override
    public void update() {

    }
}
