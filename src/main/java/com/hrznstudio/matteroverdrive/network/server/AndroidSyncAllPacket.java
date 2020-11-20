package com.hrznstudio.matteroverdrive.network.server;

import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkEvent;

public class AndroidSyncAllPacket extends Message {

    private CompoundNBT compoundNBT;

    public AndroidSyncAllPacket(CompoundNBT compoundNBT) {
        this.compoundNBT = compoundNBT;
    }

    public AndroidSyncAllPacket() {
    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.deserializeNBT(compoundNBT)));
    }
}
