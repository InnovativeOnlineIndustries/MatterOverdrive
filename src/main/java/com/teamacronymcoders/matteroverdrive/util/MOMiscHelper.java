package com.teamacronymcoders.matteroverdrive.util;

public class MOMiscHelper {

    public static int getLevelForExperience(int experience) {
        int i = 0;
        while (getExperienceForLevel(i) <= experience) {
            i++;
        }
        return i - 1;
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        }

        if (level > 0 && level < 17) {
            return level * level + 6 * level;
        } else if (level > 16 && level < 32) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    public static int getExperienceForLevelWithDestination(int currentLevel, int destination) {
        int exp = 0;
        for (; currentLevel > destination; currentLevel--) {
            if (currentLevel <= 16) {
                exp += 2 * currentLevel + 7;
            } else if (currentLevel <= 31) {
                exp += 5 * currentLevel - 38;
            } else {
                exp += 9 * currentLevel - 158;
            }
        }
        return exp;
    }

}
