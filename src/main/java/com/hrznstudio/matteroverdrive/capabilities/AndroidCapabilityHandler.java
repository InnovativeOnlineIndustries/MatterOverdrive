package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.capabilities.android.IAndroidData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MatterOverdrive.MOD_ID)
public class AndroidCapabilityHandler {

    public static void register() {
        CapabilityManager.INSTANCE.register(IAndroidData.class, NBTCapabilityStorage.create(CompoundNBT.class), AndroidData::new);
    }



}
