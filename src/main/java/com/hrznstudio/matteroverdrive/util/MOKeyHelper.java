package com.hrznstudio.matteroverdrive.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MOKeyHelper {

    @OnlyIn(Dist.CLIENT)
    public static boolean isSneakKeyDown() {
        final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;
        final long handle = Minecraft.getInstance().getMainWindow().getHandle();
        return InputMappings.isKeyDown(handle, keyBindSneak.getKey().getKeyCode());
    }

}
