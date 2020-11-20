package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MOCapabilities {

    @CapabilityInject(IAndroid.class)
    public static final Capability<IAndroid> ANDROID_DATA = null;

}
