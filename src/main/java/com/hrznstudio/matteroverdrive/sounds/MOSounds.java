package com.hrznstudio.matteroverdrive.sounds;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MOSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MatterOverdrive.MOD_ID);

    public static final RegistryObject<SoundEvent> ANDROID_TELEPORT = register("android_teleport");

    private static RegistryObject<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> new SoundEvent(new ResourceLocation(MatterOverdrive.MOD_ID, key)));
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
