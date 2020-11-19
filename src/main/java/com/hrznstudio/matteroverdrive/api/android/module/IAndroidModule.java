package com.hrznstudio.matteroverdrive.api.android.module;

import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IAndroidModule {

    /**
     * This returns a boolean check against this modifier.
     *
     * @param module The module being passed to check against this.
     * @return Returns if the passed module can be applied with this.
     */
    boolean canApplyTogether(IAndroidModule module);

    /**
     * This is used to set the stored data using a NBT-tag on Install.
     *
     * @param nbt The NBT being passed for updating the Module.
     */
    default void onInstall(LivingEntity entity, AndroidData data, CompoundNBT nbt) {
    }

    /**
     * This is used to update the Modules internal data using passed NBT-Data.
     *
     * @param nbt The NBT being passed for updating the Module.
     */
    default void onUpdate(LivingEntity entity, AndroidData data, CompoundNBT nbt) {
    }

    /**
     * This is used to set the stored data using a NBT-tag on Removal.
     *
     * @param nbt The NBT being passed for updating the Module.
     */
    default void onRemoval(LivingEntity entity, AndroidData data, CompoundNBT nbt) {
    }

    /**
     * This returns a boolean check against both Modifiers not just this Modifier.
     *
     * @param modifier Modifier to check against.
     * @return Returns the final value if this can be applied together with the other Modifier.
     */
    default boolean isCompatibleWith(IAndroidModule modifier) {
        return this.canApplyTogether(modifier) && modifier.canApplyTogether(this);
    }
}
