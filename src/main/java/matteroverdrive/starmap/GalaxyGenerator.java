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

package matteroverdrive.starmap;

import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.starmap.gen.PlanetDwarfGen;
import matteroverdrive.starmap.gen.PlanetGasGiantGen;
import matteroverdrive.starmap.gen.PlanetNormalGen;
import matteroverdrive.starmap.gen.StarGen;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: move the galaxy generator to a clib galaxy plugin
public class GalaxyGenerator implements IConfigSubscriber {
    //region Private Vars
    private final Random random;
    private final Random starRandom;
    private final Random planetRandom;
    private final Random starNameRandom;
    private final WeightedRandomSpaceGen<Planet> planetGen;
    private final WeightedRandomSpaceGen<Star> starGen;
    private float StarPrefixChance = 1;
    private float StarSufixChance = 0.8f;
    private int maxStars = 2048 + 256;
    private int minStars = 2048;
    private int minPlanets = 1;
    private int maxPlanets = 4;
    private int quadrantCount = 3;
    //endregion

    //region Constructors
    public GalaxyGenerator() {
        random = new Random();
        starRandom = new Random();
        planetRandom = new Random();
        starNameRandom = new Random();
        planetGen = new WeightedRandomSpaceGen(random);
        starGen = new WeightedRandomSpaceGen(random);
        planetGen.addGen(new PlanetGasGiantGen());
        planetGen.addGen(new PlanetDwarfGen());
        planetGen.addGen(new PlanetNormalGen());
        starGen.addGens(StarGen.getStarGens());
    }
    //endregion

    //region Single Generation
    public Galaxy generateGalaxy(String name, int id, long seed, World world) {
        Galaxy galaxy = new Galaxy(name, id, seed, world);
        random.setSeed(seed);
        generateQuadrants(galaxy, quadrantCount);
        return galaxy;
    }

    public void generateStar(Star star, boolean forced, boolean generatePLanets) {
        if (star.isGenerated() || forced) {
            star.clearPlanets();
            starRandom.setSeed(star.getSeed());
            Vec3d starPos = generateStarPosition(starRandom);
            star.setPosition((float) starPos.x, (float) starPos.y, (float) starPos.z);
            starGen.getRandomGen(star, starRandom).generateSpaceBody(star, starRandom);
            if (generatePLanets) {
                generatePlanets(star, minPlanets + random.nextInt(maxPlanets - minPlanets));
            }
        }
    }

    public void generatePlanet(Planet planet, boolean forced) {
        if (planet.isGenerated() || forced) {
            planetRandom.setSeed(planet.getSeed());
            float orbit = planetRandom.nextFloat();
            planet.setOrbit(orbit);
            planetGen.getRandomGen(planet, planetRandom).generateSpaceBody(planet, planetRandom);
        }
    }
    //endregion

    //region Massive Generation
    public void generateQuadrants(Galaxy galaxy, int size3d) {
        starNameRandom.setSeed(galaxy.getSeed());
        List<Star> stars = generateStars(minStars + random.nextInt(maxStars - minStars));
        Quadrant[] quadrants = new Quadrant[size3d * size3d * size3d];

        float quadrantPosPeace = 2f / (float) size3d;

        for (int i = 0; i < quadrants.length; i++) {
            float z = (i % size3d) * quadrantPosPeace - 1;
            float y = ((i / size3d) % size3d) * quadrantPosPeace - 1;
            float x = (i / (size3d * size3d)) * quadrantPosPeace - 1;

            quadrants[i] = new Quadrant("Q" + i, i);
            galaxy.addQuadrant(quadrants[i]);
            quadrants[i].setGalaxy(galaxy);
            quadrants[i].setSize(quadrantPosPeace);
            quadrants[i].setPosition(x, y, z);
        }

        for (Star star : stars) {
            for (Quadrant quadrant : quadrants) {
                if (star.getX() >= quadrant.getX() && star.getX() < quadrant.getX() + quadrantPosPeace
                        && star.getY() >= quadrant.getY() && star.getY() < quadrant.getY() + quadrantPosPeace
                        && star.getZ() >= quadrant.getZ() && star.getZ() < quadrant.getZ() + quadrantPosPeace) {
                    star.setId(quadrant.getStars().size());
                    quadrant.addStar(star);
                    star.setQuadrant(quadrant);
                }
            }
        }
    }

    public List<Star> generateStars(int amount) {
        List<Star> stars = new ArrayList<>(amount);
        List<String> names = StarGen.generateAvailableNames(starNameRandom, 18, StarPrefixChance, StarSufixChance);

        for (int i = 0; i < amount; i++) {
            Star star = new Star(names.get(i), i);
            int seed = random.nextInt();
            star.setSeed(seed);
            stars.add(star);
            generateStar(star, true, true);
        }
        return stars;
    }

    public void generatePlanets(Star star, int amount) {
        for (int i = 0; i < amount; i++) {
            Planet planet = new Planet(star.getSpaceBodyName() + " " + i, i);
            planet.setSeed(random.nextInt());
            star.addPlanet(planet);
            planet.setStar(star);
            generatePlanet(planet, true);
        }
    }
    //endregion

    //region Regeneration
    public void regenerateQuadrants(Galaxy galaxy) {
        for (Quadrant quadrant : galaxy.getQuadrants()) {
            regenerateStars(quadrant);
        }
    }

    public void regenerateStars(Quadrant quadrant) {
        for (Star star : quadrant.getStars()) {
            if (!star.isClaimed()) {
                starRandom.setSeed(star.getSeed());
                starGen.getRandomGen(star).generateSpaceBody(star, starRandom);
                regeneratePlanets(star);
            }
        }
    }

    public void regeneratePlanets(Star star) {
        for (Planet planet : star.getPlanets()) {
            generatePlanet(planet, true);
        }
    }
    //endregion

    //region Other Gen
    public Vec3d generateStarPosition(Random random) {
        double x = MathHelper.clamp(MOMathHelper.nextGaussian(random, 0, 1d / 3d), -1, 1);
        double y = MathHelper.clamp(MOMathHelper.nextGaussian(random, 0, 1d / 3d), -1, 1);
        double z = MathHelper.clamp(MOMathHelper.nextGaussian(random, 0, 1d / 3d), -1, 1);
        return new Vec3d(x, y, z);
    }
    //endregion

    //region Events
    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        StarPrefixChance = config.config.getFloat("name_prefix_chance", ConfigurationHandler.CATEGORY_STARMAP, 1, 0, 1, "The chance of adding a prefix to a Star System's name");
        StarSufixChance = config.config.getFloat("name_suffix_chance", ConfigurationHandler.CATEGORY_STARMAP, 0.8f, 0, 1, "The chance of adding a suffix to a Star System's name");
        minStars = config.config.getInt("min_star_count", ConfigurationHandler.CATEGORY_STARMAP, 2048, 0, 512000, "The minimum amount of stars in a galaxy");
        maxStars = config.config.getInt("max_star_count", ConfigurationHandler.CATEGORY_STARMAP, 2048 + 256, 0, 512000, "The maximum amount of stars in a galaxy");
        minPlanets = config.config.getInt("min_planet_count", ConfigurationHandler.CATEGORY_STARMAP, 1, 0, 8, "The minimum amount of planets per star system");
        maxPlanets = config.config.getInt("max_planet_count", ConfigurationHandler.CATEGORY_STARMAP, 4, 0, 8, "The maximum amount of planets pre star system");
        quadrantCount = config.config.getInt("quadrant_count", ConfigurationHandler.CATEGORY_STARMAP, 3, 1, 6, "The amount of quadrants the galaxy should be divided into. The amount is cubed. x ^ 3. For example 3 ^ 3 = 27 quadrants.");
    }
    //endregion

    //region Getters and Setters
    public WeightedRandomSpaceGen<Planet> getPlanetGen() {
        return planetGen;
    }

    public WeightedRandomSpaceGen<Star> getStarGen() {
        return starGen;
    }

    public Random getPlanetRandom() {
        return planetRandom;
    }

    public Random getStarRandom() {
        return starRandom;
    }
    //endregion


}
