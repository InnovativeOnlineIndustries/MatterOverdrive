package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidDataProvider;
import com.hrznstudio.matteroverdrive.capabilities.android.IAndroidData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MatterOverdrive.MOD_ID)
public class AndroidCapabilityHandler {

    public static void register() {
        CapabilityManager.INSTANCE.register(IAndroidData.class, NBTCapabilityStorage.create(CompoundNBT.class), AndroidData::new);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MatterOverdrive.MOD_ID, "android_data"), new AndroidDataProvider());
        }
    }


}
