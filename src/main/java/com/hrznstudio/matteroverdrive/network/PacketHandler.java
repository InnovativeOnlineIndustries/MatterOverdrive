package com.hrznstudio.matteroverdrive.network;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.network.client.AndroidPerkAttemptBuyMessage;
import com.hrznstudio.matteroverdrive.network.server.AndroidSyncAllPacket;
import com.hrznstudio.matteroverdrive.network.server.AndroidTurningTimeSyncPacket;
import com.hrznstudio.titanium.network.NetworkHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    public static final String PROTOCOL_VERSION = "1";

    public static NetworkHandler NETWORK = new NetworkHandler(MatterOverdrive.MOD_ID);

    public static void init() {
        NETWORK.registerMessage(AndroidTurningTimeSyncPacket.class);
        NETWORK.registerMessage(AndroidSyncAllPacket.class);
        NETWORK.registerMessage(AndroidPerkAttemptBuyMessage.class);
        NETWORK.registerMessage(AndroidPerkAttemptBuyMessage.Reply.class);
    }

    public static void sendToPlayer(Object obj, ServerPlayerEntity player) {
        NETWORK.get().sendTo(obj, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

}
