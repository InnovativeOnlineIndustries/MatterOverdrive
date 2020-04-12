/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.api.weapon.WeaponStats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WeaponModuleSniperScope extends WeaponModuleBase implements IWeaponScope {
    public WeaponModuleSniperScope(String name) {
        super(name);
        setHasSubtypes(false);
        applySlot(Reference.MODULE_SIGHTS);
        applyWeaponStat(0, WeaponStats.RANGE, 1.5f);
    }

    @Override
    public float getZoomAmount(ItemStack scopeStack, ItemStack weapon) {
        return 0.85f;
    }

    @Override
    public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed, float originalAccuracy) {
        if (zoomed) {
            return originalAccuracy * 0.4f;
        }
        return originalAccuracy + 4f;
    }

    @Override
    public String getModelPath() {
        return Reference.PATH_MODEL_ITEMS + "sniper_scope.obj";
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return new ResourceLocation(Reference.PATH_ITEM + "sniper_scope_texture.png");
    }

    @Override
    public String getModelName(ItemStack module) {
        return "sniper_scope";
    }
}
