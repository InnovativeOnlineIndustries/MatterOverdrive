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

package matteroverdrive.entity.monster;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class EntityRogueAndroid implements IConfigSubscriber {
    public static final HashSet<Integer> dimensionBlacklist = new HashSet<>();
    public static final HashSet<Integer> dimensionWhitelist = new HashSet<>();
    private static final HashSet<String> biomesBlacklist = new HashSet<>();
    private static final HashSet<String> biomesWhitelist = new HashSet<>();
    private static final List<Biome.SpawnListEntry> spawnListEntries = new ArrayList<>();
    public static float LEGENDARY_SPAWN_CHANCE;
    public static float SPAWN_CHANCE;
    public static int MAX_ANDROIDS_PER_CHUNK = 4;
    public static boolean dropItems;

    public static void addAsBiomeGen(Class<? extends EntityLiving> entityClass) {
        spawnListEntries.add(new Biome.SpawnListEntry(entityClass, 15, 1, 2));
        addInBiome(Biome.REGISTRY.iterator());
    }

    private static void addInBiome(Iterator<Biome> biomes) {
        loadBiomeBlacklist(MatterOverdrive.CONFIG_HANDLER);
        loadBiomesWhitelist(MatterOverdrive.CONFIG_HANDLER);

        while (biomes.hasNext()) {
            Biome biome = biomes.next();
            if (biome != null) {
                List spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
                for (Biome.SpawnListEntry entry : spawnListEntries) {
                    if (isBiomeValid(biome) && !spawnList.contains(entry) && entry.itemWeight > 0) {
                        spawnList.add(entry);
                    }
                }

            }
        }
    }

    private static boolean isBiomeValid(Biome biome) {
        if (biome != null) {
            if (biomesWhitelist.size() > 0) {
                return biomesWhitelist.contains(biome.getRegistryName().toString());
            } else {
                return !biomesBlacklist.contains(biome.getRegistryName().toString());
            }
        }
        return false;
    }

    private static void loadBiomeBlacklist(ConfigurationHandler config) {
        biomesBlacklist.clear();
        String[] blacklist = config.config.getStringList("biome.blacklist", ConfigurationHandler.CATEGORY_ENTITIES + ".rogue_android", new String[]{"minecraft:hell", "minecraft:sky", "minecraft:mushroom_island", "minecraft:mushroom_island_shore"}, "Rogue Android biome blacklist");
        for (String aBlacklist : blacklist) {
            biomesBlacklist.add(aBlacklist.toLowerCase());
        }
    }

    private static void loadBiomesWhitelist(ConfigurationHandler configurationHandler) {
        biomesWhitelist.clear();
        String[] whitelist = configurationHandler.config.getStringList("biome.whitelist", ConfigurationHandler.CATEGORY_ENTITIES + "." + "rogue_android", new String[0], "Rogue Android biome whitelist");
        for (String aWhitelist : whitelist) {
            biomesBlacklist.add(aWhitelist.toLowerCase());
        }
    }

    private static void loadDimensionBlacklist(ConfigurationHandler configurationHandler) {
        dimensionBlacklist.clear();
        Property blacklistProp = configurationHandler.config.get(ConfigurationHandler.CATEGORY_ENTITIES + "." + "rogue_android", "dimension.blacklist", new int[]{1});
        blacklistProp.setComment("Rogue Android Dimension ID blacklist");
        int[] blacklist = blacklistProp.getIntList();
        for (int aBlacklist : blacklist) {
            dimensionBlacklist.add(aBlacklist);
        }
    }

    private static void loadDimesionWhitelist(ConfigurationHandler configurationHandler) {
        dimensionWhitelist.clear();
        Property whitelistProp = configurationHandler.config.get(ConfigurationHandler.CATEGORY_ENTITIES + ".rogue_android", "dimension.whitelist", new int[0]);
        whitelistProp.setComment("Rogue Android Dimension ID whitelist");
        int[] whitelist = whitelistProp.getIntList();

        for (int item : whitelist) {
            dimensionWhitelist.add(item);
        }
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        String category = ConfigurationHandler.CATEGORY_ENTITIES + ".rogue_android";

        int spawn_weight = config.config.getInt("spawn_weight", category, 25, 0, 100, "The spawn weight of Androids. This controls how likely are to be chosen to spawn next.");

        for (Biome.SpawnListEntry entry : spawnListEntries) {
            entry.itemWeight = spawn_weight;
        }

        loadDimensionBlacklist(config);
        loadDimesionWhitelist(config);
        loadBiomeBlacklist(config);
        loadBiomesWhitelist(config);

        EntityRangedRogueAndroidMob.UNLIMITED_WEAPON_ENERGY = config.getBool("unlimited_weapon_energy", category, true, "Do Ranged Rogue Androids have unlimited weapon energy in their weapons");
        MAX_ANDROIDS_PER_CHUNK = config.getInt("max_android_per_chunk", category, 4, "The max amount of Rogue Android that can spawn in a given chunk");
        SPAWN_CHANCE = config.config.getFloat("spawn_chance_percent", category, 0.1f, 0, 1, "The spawn chance of rogue androids. How likely are they to spawn once chosen to spawn.");
        EntityRangedRogueAndroidMob.DROP_NORMAL_WEAPONS = config.getBool("drop_weapons", category, true, "Should normal Rogue Androids drop their weapons? If set to false they will never drop their weapons, but if set to true there is a small chance they will drop them.");
        EntityRangedRogueAndroidMob.DROP_LEGENDARY_WEAPONS = config.getBool("drop_legendary_weapons", category, true, "Should Legendary rogue androids drop Legendary weapons");
        LEGENDARY_SPAWN_CHANCE = config.config.getFloat("legendary_spawn_chance_percent", category, 0.03f, 0, 1, "The chance in percent, of rogue androids becoming legendary. This is the base value. This value is multiplied by the android's level");
        dropItems = config.config.getBoolean("do_drops", category, true, "Should the Rouge Androids drop any items");
    }
}