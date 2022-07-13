package com.hrznstudio.matteroverdrive.capabilities;

import com.hrznstudio.matteroverdrive.api.android.IAndroid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class MOCapabilities {

    public static final Capability<IAndroid> ANDROID_DATA = CapabilityManager.get(new CapabilityToken<>() {});

}
