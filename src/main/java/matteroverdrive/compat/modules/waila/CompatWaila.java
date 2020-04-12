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

package matteroverdrive.compat.modules.waila;

import matteroverdrive.blocks.BlockBoundingBox;
import matteroverdrive.blocks.BlockDecomposer;
import matteroverdrive.blocks.BlockFusionReactorController;
import matteroverdrive.blocks.BlockReplicator;
import matteroverdrive.blocks.BlockStarMap;
import matteroverdrive.blocks.BlockTransporter;
import matteroverdrive.blocks.BlockWeaponStation;
import matteroverdrive.compat.Compat;
/*import matteroverdrive.compat.modules.waila.provider.BoundingBox;
import matteroverdrive.compat.modules.waila.provider.Matter;
import matteroverdrive.compat.modules.waila.provider.Replicator;
import matteroverdrive.compat.modules.waila.provider.StarMap;
import matteroverdrive.compat.modules.waila.provider.Transporter;
import matteroverdrive.compat.modules.waila.provider.WeaponStation;*/
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * Compatibility for WAILA
 *
 * @author shadowfacts
 */
@Compat("waila")
public class CompatWaila {

	/*
	 * @Compat.Init public static void init(FMLInitializationEvent event) {
	 * FMLInterModComms.sendMessage("waila", "register",
	 * "matteroverdrive.compat.modules.waila.CompatWaila.registerCallback"); }
	 * 
	 * public static void registerCallback(IWailaRegistrar registrar) {
	 * registrar.registerBodyProvider(new WeaponStation(),
	 * BlockWeaponStation.class); registrar.registerBodyProvider(new StarMap(),
	 * BlockStarMap.class); registrar.registerBodyProvider(new Transporter(),
	 * BlockTransporter.class); registrar.registerBodyProvider(new Matter(),
	 * BlockDecomposer.class); registrar.registerBodyProvider(new Replicator(),
	 * BlockReplicator.class); registrar.registerBodyProvider(new Matter(),
	 * BlockFusionReactorController.class);
	 * 
	 * registrar.registerStackProvider(new BoundingBox(), BlockBoundingBox.class); }
	 */

}
