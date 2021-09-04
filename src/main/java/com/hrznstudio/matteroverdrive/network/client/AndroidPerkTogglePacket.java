package com.hrznstudio.matteroverdrive.network.client;

import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.titanium.network.Message;
import net.minecraftforge.fml.network.NetworkEvent;

public class AndroidPerkTogglePacket extends Message {

    public String perk;

    public AndroidPerkTogglePacket(String perk) {
        this.perk = perk;
    }

    public AndroidPerkTogglePacket() {
    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            context.getSender().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                iAndroid.getPerkManager().togglePerk(perk);
                iAndroid.requestUpdate();
            });
        });
    }
}
