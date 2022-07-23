package com.teamacronymcoders.matteroverdrive.api.weapon;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.gameevent.BlockPositionSource;

public class WeaponSound extends AbstractTickableSoundInstance implements TickableSoundInstance {

  private boolean isDonePlaying;

  protected WeaponSound(SoundEvent event, SoundSource source, RandomSource random, float x, float y, float z, float volume, float pitch) {
    super(event, source, random);
    setPosition(x, y, z);

  }

  @Override
  public void tick() {}

  public void isLooping(boolean looping) {
    this.looping = looping;
  }

  public void setPosition(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

}
