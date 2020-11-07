package com.hrznstudio.matteroverdrive.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class MOCapabilityHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(IAndroidData.class, NBTCapabilityStorage.create(CompoundNBT.class), AndroidData::new);
    }

}
