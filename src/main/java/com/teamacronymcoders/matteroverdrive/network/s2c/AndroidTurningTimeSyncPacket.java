package com.teamacronymcoders.matteroverdrive.network.s2c;

import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AndroidTurningTimeSyncPacket {

    private final int time;

    public AndroidTurningTimeSyncPacket(int time) {
        this.time = time;
    }

    public static Handler HANDLER = new Handler();

    private static class Handler implements NetworkHandler<AndroidTurningTimeSyncPacket> {
        public void encode(AndroidTurningTimeSyncPacket msg, FriendlyByteBuf outBuffer) {
            outBuffer.writeInt(msg.time);
        }

        public AndroidTurningTimeSyncPacket decode(FriendlyByteBuf inBuffer) {
            return new AndroidTurningTimeSyncPacket(inBuffer.readInt());
        }

        public void handle(AndroidTurningTimeSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.setTurningTime(msg.time));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
