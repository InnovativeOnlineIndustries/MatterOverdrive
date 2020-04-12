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

package matteroverdrive.compat.modules;

import matteroverdrive.compat.Compat;
import matteroverdrive.init.OverdriveFluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Compat(CompatTConstruct.ID)
public class CompatTConstruct {

    public static final String ID = "tconstruct";

    @Compat.PreInit
    public static void preInit(FMLPreInitializationEvent event) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("fluid", OverdriveFluids.moltenTritanium.getName());
        tag.setString("ore", "Tritanium");
        tag.setBoolean("toolforge", true);

        FMLInterModComms.sendMessage(ID, "integrateSmeltery", tag);
    }

}
