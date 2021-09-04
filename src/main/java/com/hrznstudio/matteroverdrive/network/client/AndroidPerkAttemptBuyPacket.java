package com.hrznstudio.matteroverdrive.network.client;

import com.hrznstudio.matteroverdrive.api.android.perk.AndroidPerkManager;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class AndroidPerkAttemptBuyPacket extends Message {

    public String perk;


    public AndroidPerkAttemptBuyPacket() {
    }

    public AndroidPerkAttemptBuyPacket(String perk) {
        this.perk = perk;
    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayerEntity entity = context.getSender();
            IAndroidPerk androidPerk = IAndroidPerk.PERKS.get(perk);
            entity.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                AndroidPerkManager perkManager = iAndroid.getPerkManager();
                if (!perkManager.hasPerk(androidPerk) || perkManager.getOwned().get(androidPerk.getName()) < androidPerk.getMaxLevel()){
                    int requiredXP = androidPerk.getRequiredXP(iAndroid, perkManager.hasPerk(androidPerk) ? perkManager.getOwned().get(androidPerk.getName()) + 1 : 1);
                    if (entity.experienceLevel >= requiredXP){
                        entity.addExperienceLevel(-requiredXP);
                        perkManager.buyPerk(androidPerk);
                        PacketHandler.NETWORK.get().sendTo(new Reply(), entity.connection.netManager,  NetworkDirection.PLAY_TO_CLIENT);
                        iAndroid.requestUpdate();
                    }
                }
            });
        });
    }

    public static class Reply extends Message{

        public Reply() {

        }

        @Override
        protected void handleMessage(NetworkEvent.Context context) {
            Minecraft.getInstance().player.playSound(MOSounds.PERK_UNLOCK.get(), 0.5f, 1f);
        }
    }
}
