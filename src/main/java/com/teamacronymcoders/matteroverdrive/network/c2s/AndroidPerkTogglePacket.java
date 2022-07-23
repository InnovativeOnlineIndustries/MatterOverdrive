package com.teamacronymcoders.matteroverdrive.network.c2s;

import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AndroidPerkTogglePacket {

    private final String perk;

    public AndroidPerkTogglePacket(String perk) {
        this.perk = perk;
    }

    public static AndroidPerkTogglePacket.Handler HANDLER = new AndroidPerkTogglePacket.Handler();

    private static class Handler implements NetworkHandler<AndroidPerkTogglePacket> {
        public void encode(AndroidPerkTogglePacket msg, FriendlyByteBuf outBuffer) {
            outBuffer.writeUtf(msg.perk);
        }

        public AndroidPerkTogglePacket decode(FriendlyByteBuf inBuffer) {
            return new AndroidPerkTogglePacket(inBuffer.readUtf());
        }

        public void handle(AndroidPerkTogglePacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ctx.get().getSender().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                    iAndroid.getPerkManager().togglePerk(msg.perk);
                    iAndroid.requestUpdate();
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
