package com.hrznstudio.matteroverdrive.network;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.network.client.AndroidPerkAttemptBuyPacket;
import com.hrznstudio.matteroverdrive.network.client.AndroidPerkTogglePacket;
import com.hrznstudio.matteroverdrive.network.server.AndroidEnergySyncPacket;
import com.hrznstudio.matteroverdrive.network.server.AndroidSyncAllPacket;
import com.hrznstudio.matteroverdrive.network.server.AndroidTurningTimeSyncPacket;
import com.hrznstudio.titanium.network.NetworkHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;

public class PacketHandler {

    public static final String PROTOCOL_VERSION = "1";

    public static NetworkHandler NETWORK = new NetworkHandler(MatterOverdrive.MOD_ID);

    public static void init() {
        NETWORK.registerMessage(AndroidTurningTimeSyncPacket.class);
        NETWORK.registerMessage(AndroidSyncAllPacket.class);
        NETWORK.registerMessage(AndroidPerkAttemptBuyPacket.class);
        NETWORK.registerMessage(AndroidPerkAttemptBuyPacket.Reply.class);
        NETWORK.registerMessage(AndroidEnergySyncPacket.class);
        NETWORK.registerMessage(AndroidPerkTogglePacket.class);
    }

    public static void sendToPlayer(Object obj, ServerPlayerEntity player) {
        NETWORK.get().sendTo(obj, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

}
