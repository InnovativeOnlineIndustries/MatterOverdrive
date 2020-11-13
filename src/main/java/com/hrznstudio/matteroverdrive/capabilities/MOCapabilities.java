package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.capabilities.android.IAndroidData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MOCapabilities {

    @CapabilityInject(IAndroidData.class)
    public static final Capability<IAndroidData> ANDROID_DATA = null;

}
