package com.hrznstudio.matteroverdrive.network.server;

import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

public class AndroidTurningTimeSyncPacket extends Message {

    private int time;

    public AndroidTurningTimeSyncPacket(int time) {
        this.time = time;
    }

    public AndroidTurningTimeSyncPacket() {

    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.setTurningTime(time));
        });
    }
}
