package com.hrznstudio.matteroverdrive.network.server;

import com.hrznstudio.matteroverdrive.capabilities.AndroidEnergyCapability;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkEvent;

public class AndroidEnergySyncPacket extends Message {

    private int energy;
    private int maxEnergy;

    public AndroidEnergySyncPacket() {
    }

    public AndroidEnergySyncPacket(int energy, int maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                if (energyStorage instanceof AndroidEnergyCapability) {
                    ((AndroidEnergyCapability) energyStorage).setEnergy(energy);
                }
            });
        });
    }
}
