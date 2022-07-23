package com.teamacronymcoders.matteroverdrive.network.s2c;

import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AndroidSyncAllPacket {

    private final CompoundTag compoundTag;

    public AndroidSyncAllPacket(CompoundTag compoundTag) {
        this.compoundTag = compoundTag;
    }

    public static AndroidSyncAllPacket.Handler HANDLER = new AndroidSyncAllPacket.Handler();

    private static class Handler implements NetworkHandler<AndroidSyncAllPacket> {
        public void encode(AndroidSyncAllPacket msg, FriendlyByteBuf outBuffer) {
            outBuffer.writeNbt(msg.compoundTag);
        }

        public AndroidSyncAllPacket decode(FriendlyByteBuf inBuffer) {
            return new AndroidSyncAllPacket(inBuffer.readNbt());
        }

        public void handle(AndroidSyncAllPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> iAndroid.deserializeNBT(msg.compoundTag));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
