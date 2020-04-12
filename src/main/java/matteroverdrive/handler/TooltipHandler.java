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

package matteroverdrive.handler;

import matteroverdrive.api.events.MOEventMatterTooltip;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class TooltipHandler {
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            MOEventMatterTooltip tooltipEvent = new MOEventMatterTooltip(event.getItemStack(), MatterHelper.getMatterAmountFromItem(event.getItemStack()), event.getEntityPlayer());
            if (!MinecraftForge.EVENT_BUS.post(tooltipEvent)) {
                if (tooltipEvent.matter > 0) {
                    event.getToolTip().add(TextFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + TextFormatting.GOLD + MatterHelper.formatMatter(tooltipEvent.matter));
                } else {
                    event.getToolTip().add(TextFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + TextFormatting.RED + MOStringHelper.translateToLocal("gui.tooltip.matter.none"));
                }
            }
        }
    }
}
