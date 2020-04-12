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

package matteroverdrive.commands;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.world.MOImageGen;
import matteroverdrive.world.buildings.WeightedRandomMOWorldGenBuilding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorldGenCommands extends CommandBase {

    @Override
    public String getName() {
        return "mo_gen";
    }

    @Override
    public String getUsage(ICommandSender p_71518_1_) {
        return "mo_gen <command> <structure name> <options>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] parameters) throws CommandException {
        EntityPlayer entityPlayer = null;
        if (parameters.length >= 4) {
            entityPlayer = getPlayer(server, commandSender, parameters[3]);
        } else if (commandSender instanceof EntityPlayer) {
            entityPlayer = (EntityPlayer) commandSender;
        }
        boolean forceGeneration = false;
        if (parameters.length >= 3) {
            forceGeneration = parameters[2].contains("f");
        }

        if (parameters.length >= 1) {
            if (parameters[0].equalsIgnoreCase("generate")) {
                if (parameters.length >= 2 && entityPlayer != null) {
                    for (WeightedRandomMOWorldGenBuilding entry : MatterOverdrive.MO_WORLD.worldGen.buildings) {
                        if (entry.worldGenBuilding.getName().equalsIgnoreCase(parameters[1])) {
                            MOImageGen.ImageGenWorker worker = MatterOverdrive.MO_WORLD.worldGen.startBuildingGeneration(entry.worldGenBuilding, entityPlayer.getPosition(), entityPlayer.getRNG(), commandSender.getEntityWorld(), null, null, forceGeneration);
                            if (worker != null) {
                                worker.setPlaceNotify(2);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            commands.add("generate");
        } else if (args.length == 2) {
            for (WeightedRandomMOWorldGenBuilding entry : MatterOverdrive.MO_WORLD.worldGen.buildings) {
                commands.add(entry.worldGenBuilding.getName());
            }
        } else if (args.length == 4) {
            for (Object entityPlayer : sender.getEntityWorld().playerEntities) {
                commands.add(((EntityPlayer) entityPlayer).getName());
            }
        }
        return commands;
    }
}
