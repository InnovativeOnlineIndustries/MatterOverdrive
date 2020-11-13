package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.capabilities.android.IAndroidData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class MOCapabilityHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(IAndroidData.class, NBTCapabilityStorage.create(CompoundNBT.class), AndroidData::new);
    }

}
