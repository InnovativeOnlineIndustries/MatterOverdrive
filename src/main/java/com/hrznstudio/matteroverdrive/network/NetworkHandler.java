package com.hrznstudio.matteroverdrive.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface NetworkHandler<T> {

    void encode(T msg, FriendlyByteBuf outBuffer);

    T decode(FriendlyByteBuf inBuffer);

    void handle(T msg, Supplier<NetworkEvent.Context> ctx);
}
