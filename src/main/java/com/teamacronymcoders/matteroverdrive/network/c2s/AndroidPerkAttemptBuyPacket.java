package com.teamacronymcoders.matteroverdrive.network.c2s;

import com.teamacronymcoders.matteroverdrive.api.android.perk.AndroidPerkManager;
import com.teamacronymcoders.matteroverdrive.api.android.perk.IAndroidPerk;
import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.network.NetworkHandler;
import com.teamacronymcoders.matteroverdrive.sounds.MOSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AndroidPerkAttemptBuyPacket {

    public String perk;


    public AndroidPerkAttemptBuyPacket() {
    }

    public AndroidPerkAttemptBuyPacket(String perk) {
        this.perk = perk;
    }

    public static AndroidPerkAttemptBuyPacket.Handler HANDLER = new AndroidPerkAttemptBuyPacket.Handler();

    private static class Handler implements NetworkHandler<AndroidPerkAttemptBuyPacket> {
        public void encode(AndroidPerkAttemptBuyPacket msg, FriendlyByteBuf outBuffer) {
            outBuffer.writeUtf(msg.perk);
        }

        public AndroidPerkAttemptBuyPacket decode(FriendlyByteBuf inBuffer) {
            return new AndroidPerkAttemptBuyPacket(inBuffer.readUtf());
        }

        public void handle(AndroidPerkAttemptBuyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer entity = ctx.get().getSender();
                IAndroidPerk androidPerk = IAndroidPerk.PERKS.get(msg.perk);
                entity.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                    AndroidPerkManager perkManager = iAndroid.getPerkManager();
                    if (!perkManager.hasPerk(androidPerk) || perkManager.getOwned().get(androidPerk.getName()) < androidPerk.getMaxLevel()){
                        int requiredXP = androidPerk.getRequiredXP(iAndroid, perkManager.hasPerk(androidPerk) ? perkManager.getOwned().get(androidPerk.getName()) + 1 : 1);
                        if (entity.experienceLevel >= requiredXP){
                            entity.giveExperienceLevels(-requiredXP);
                            perkManager.buyPerk(androidPerk);
                            entity.level.playSound(entity, entity.blockPosition(), MOSounds.PERK_UNLOCK.get(), SoundSource.PLAYERS, 0.5f, 1f);
                            iAndroid.requestUpdate();
                        }
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
