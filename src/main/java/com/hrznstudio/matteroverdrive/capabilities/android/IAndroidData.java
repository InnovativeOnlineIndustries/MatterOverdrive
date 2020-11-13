package com.hrznstudio.matteroverdrive.capabilities.android;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAndroidData extends INBTSerializable<CompoundNBT> {
    float getChakra();
    float getStamina();
    void setChakra(float chakra);
    void setStamina(float stamina);

}
