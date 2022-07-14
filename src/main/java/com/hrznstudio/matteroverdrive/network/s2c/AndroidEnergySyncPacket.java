package com.hrznstudio.matteroverdrive.network.s2c;

import com.hrznstudio.matteroverdrive.capabilities.android.AndroidEnergy;
import com.hrznstudio.matteroverdrive.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AndroidEnergySyncPacket {

    private final int energy;
    private final int maxEnergy;

    public AndroidEnergySyncPacket(int energy, int maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    public static AndroidEnergySyncPacket.Handler HANDLER = new AndroidEnergySyncPacket.Handler();

    private static class Handler implements NetworkHandler<AndroidEnergySyncPacket> {
        public void encode(AndroidEnergySyncPacket msg, FriendlyByteBuf outBuffer) {
            outBuffer.writeInt(msg.energy);
            outBuffer.writeInt(msg.maxEnergy);
        }

        public AndroidEnergySyncPacket decode(FriendlyByteBuf inBuffer) {
            return new AndroidEnergySyncPacket(inBuffer.readInt(), inBuffer.readInt());
        }

        public void handle(AndroidEnergySyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft.getInstance().player.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                    if (energyStorage instanceof AndroidEnergy) {
                        ((AndroidEnergy) energyStorage).setEnergy(msg.energy);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
