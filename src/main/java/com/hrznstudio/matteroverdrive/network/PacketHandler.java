package com.hrznstudio.matteroverdrive.network;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.network.client.ClientTestPacket;
import com.hrznstudio.matteroverdrive.network.server.ServerTestPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel TEST_CHANNEL = NetworkRegistry.ChannelBuilder
        .named(new ResourceLocation(MatterOverdrive.MOD_ID, "android_data"))
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
        .simpleChannel();

    public static void init() {
        TEST_CHANNEL.registerMessage(0, ClientTestPacket.class, ClientTestPacket::encode, ClientTestPacket::decode, ClientTestPacket.Handler::handle);
        TEST_CHANNEL.registerMessage(100, ServerTestPacket.class, ServerTestPacket::encode, ServerTestPacket::decode, ServerTestPacket.Handler::handle);
    }

    public static void sendToPlayer(Object obj, ServerPlayerEntity player) {
        TEST_CHANNEL.sendTo(obj, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

}
