/*
 * This file is part of MatterOverdrive: Legacy Edition Copyright (C) 2019,
 * Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Matter Overdrive. If not, see <http://www.gnu.org/licenses>.
 * 
 * 
 * package matteroverdrive.compat.modules.waila.provider;
 * 
 * import matteroverdrive.compat.modules.waila.IWailaBodyProvider; import
 * matteroverdrive.tile.TileEntityMachineStarMap; import
 * matteroverdrive.util.MOStringHelper; import
 * mcp.mobius.waila.api.IWailaConfigHandler; import
 * mcp.mobius.waila.api.IWailaDataAccessor; import net.minecraft.item.ItemStack;
 * import net.minecraft.tileentity.TileEntity; import
 * net.minecraft.util.text.TextFormatting;
 * 
 * import java.util.List;
 * 
 *//**
	 * @author shadowfacts
	 *//*
		 * public class StarMap implements IWailaBodyProvider {
		 * 
		 * private static final String[] LEVELS = new
		 * String[]{"gui.tooltip.page.galaxy", "gui.tooltip.page.quadrant",
		 * "gui.tooltip.page.star", "gui.tooltip.page.planet",
		 * "gui.tooltip.page.planet_stats"};
		 * 
		 * @Override public List<String> getWailaBody(ItemStack itemStack, List<String>
		 * currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		 * TileEntity te = accessor.getTileEntity();
		 * 
		 * if (te instanceof TileEntityMachineStarMap) { TileEntityMachineStarMap
		 * starMap = (TileEntityMachineStarMap) te;
		 * 
		 * currenttip.add(String.format("%sCurrent Mode: %s%s (%d)",
		 * TextFormatting.YELLOW, TextFormatting.WHITE,
		 * MOStringHelper.translateToLocal(LEVELS[starMap.getZoomLevel()]),
		 * starMap.getZoomLevel()));
		 * 
		 * } else { throw new
		 * RuntimeException("Star Map WAILA provider is being used for something that is not a Star Map: "
		 * + te.getClass()); }
		 * 
		 * return currenttip; }
		 * 
		 * }
		 */