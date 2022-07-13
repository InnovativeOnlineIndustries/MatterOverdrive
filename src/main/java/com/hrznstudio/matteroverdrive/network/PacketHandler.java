package com.hrznstudio.matteroverdrive.network;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.network.c2s.AndroidPerkAttemptBuyPacket;
import com.hrznstudio.matteroverdrive.network.c2s.AndroidPerkTogglePacket;
import com.hrznstudio.matteroverdrive.network.s2c.AndroidEnergySyncPacket;
import com.hrznstudio.matteroverdrive.network.s2c.AndroidSyncAllPacket;
import com.hrznstudio.matteroverdrive.network.s2c.AndroidTurningTimeSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketHandler {

    public static final String PROTOCOL_VERSION = ModList.get().getModFileById(MatterOverdrive.MOD_ID).versionString();

    private static final Logger LOGGER = LoggerFactory.getLogger("MO:PacketHandler");

    public static final SimpleChannel MO_CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MatterOverdrive.MOD_ID, "mo_channel"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(version -> {
                if (version.equals(PROTOCOL_VERSION)) {
                    return true;
                } else {
                    LOGGER.error("Client attempted to connect with a different version of the mod. Client Version: " + PROTOCOL_VERSION + " Server Version: " + version);
                    return false;
                }
            })
            .serverAcceptedVersions(version -> {
                if (version.equals(PROTOCOL_VERSION)) {
                    return true;
                } else {
                    LOGGER.error("Client attempted to connect with a different version of the mod. Server Version: " + PROTOCOL_VERSION + " Client Version: " + version);
                    return false;
                }
            })
            .simpleChannel();

    public static void init() {
        registerPacket(AndroidTurningTimeSyncPacket.class, AndroidTurningTimeSyncPacket.HANDLER);
        registerPacket(AndroidSyncAllPacket.class, AndroidSyncAllPacket.HANDLER);
        registerPacket(AndroidPerkAttemptBuyPacket.class, AndroidPerkAttemptBuyPacket.HANDLER);
        registerPacket(AndroidEnergySyncPacket.class, AndroidEnergySyncPacket.HANDLER);
        registerPacket(AndroidPerkTogglePacket.class, AndroidPerkTogglePacket.HANDLER);
    }

    private static void registerPacket(Class packetClass, NetworkHandler handler) {
        MO_CHANNEL.registerMessage(getPacketID(), packetClass, handler::encode, handler::decode, handler::handle);
    }

    private static int packetId = 0;
    /**
     * When called grabs a new packet id and increments.
     *
     * Only really usable because we force the mods to have to be the same version via PROTOCOL_VERSION
     * @return
     */
    private static int getPacketID() {
        return packetId++;
    }

    public static void sendToPlayer(Object obj, ServerPlayer player) {
        MO_CHANNEL.sendTo(obj, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object obj) {
        MO_CHANNEL.sendToServer(obj);
    }

}
