package com.teamacronymcoders.matteroverdrive.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class MOKeyHelper {

    public static boolean isSneakKeyDown() {
        final KeyMapping keyBindSneak = Minecraft.getInstance().options.keyShift;
        final long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, keyBindSneak.getKey().getValue());
    }

}
