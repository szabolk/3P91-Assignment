package UtilThings;

import java.util.Arrays;
import java.util.List;

/**
 * Holds the stat lines of all entities in the game.
 * Each list contains 3 stat lines (for level 1-3), where level 1 is the starting stat line (i.e. when
 * the entities are initialized).
 */
public class EntityLevelData {

    //Stat Order:
    //(level, hp, damage, range, productionRate, goldCost, ironCost, lumberCost, timeToCompletion, villageHallReq)

    public static final List<EntityStats> WORKER_LEVELS = Arrays.asList(
            new EntityStats(1, 100, 10, 1, 5, 50, 0, 10, 15, 1),
            new EntityStats(2, 125, 12, 1, 8, 75, 10, 20, 20, 2),
            new EntityStats(3, 150, 15, 1, 12, 100, 25, 40, 30, 3)
    );

    public static final List<EntityStats> SOLDIER_LEVELS = Arrays.asList(
            new EntityStats(1, 200, 25, 1, 0, 60, 20, 0, 20, 1),
            new EntityStats(2, 300, 40, 1, 0, 90, 40, 0, 30, 2),
            new EntityStats(3, 450, 60, 1, 0, 130, 70, 0, 45, 4)
    );

    public static final List<EntityStats> ARCHER_LEVELS = Arrays.asList(
            new EntityStats(1, 120, 18, 5, 0, 50, 0, 40, 20, 1),
            new EntityStats(2, 160, 28, 6, 0, 75, 15, 60, 30, 2),
            new EntityStats(3, 220, 42, 7, 0, 110, 30, 90, 40, 3)
    );

    public static final List<EntityStats> KNIGHT_LEVELS = Arrays.asList(
            new EntityStats(1, 500, 50, 1, 0, 150, 100, 0, 45, 3),
            new EntityStats(2, 750, 75, 1, 0, 225, 150, 0, 60, 4),
            new EntityStats(3, 1100, 110, 1, 0, 350, 250, 0, 80, 5)
    );

    public static final List<EntityStats> CATAPULT_LEVELS = Arrays.asList(
            new EntityStats(1, 300, 100, 10, 0, 200, 150, 200, 60, 4),
            new EntityStats(2, 400, 160, 11, 0, 300, 220, 300, 80, 5),
            new EntityStats(3, 550, 250, 12, 0, 450, 350, 450, 100, 6)
    );

    //Production Building Stats

    public static final List<EntityStats> GOLD_MINE_LEVELS = Arrays.asList(
            new EntityStats(1, 500, 0, 0, 20, 0, 50, 100, 60, 1),
            new EntityStats(2, 800, 0, 0, 45, 0, 100, 200, 90, 3),
            new EntityStats(3, 1200, 0, 0, 80, 0, 200, 400, 150, 5)
    );

    // Applying same logic for Iron and Lumber
    public static final List<EntityStats> IRON_MINE_LEVELS = Arrays.asList(
            new EntityStats(1, 600, 0, 0, 15, 100, 0, 100, 60, 2),
            new EntityStats(2, 900, 0, 0, 35, 200, 0, 200, 90, 4),
            new EntityStats(3, 1400, 0, 0, 65, 400, 0, 400, 150, 6)
    );

    public static final List<EntityStats> LUMBER_MILL_LEVELS = Arrays.asList(
            new EntityStats(1, 400, 0, 0, 25, 100, 50, 0, 45, 1),
            new EntityStats(2, 700, 0, 0, 55, 200, 100, 0, 75, 3),
            new EntityStats(3, 1100, 0, 0, 100, 400, 200, 0, 120, 5)
    );

    // --- DEFENSES & HUB ---

    public static final List<EntityStats> ARCHER_TOWER_LEVELS = Arrays.asList(
            new EntityStats(1, 800, 30, 8, 0, 100, 50, 150, 60, 2),
            new EntityStats(2, 1200, 50, 9, 0, 200, 100, 250, 90, 3),
            new EntityStats(3, 1800, 80, 10, 0, 400, 200, 450, 130, 5)
    );

    public static final List<EntityStats> VILLAGE_HALL_LEVELS = Arrays.asList(
            new EntityStats(1, 2000, 0, 0, 0, 500, 500, 500, 120, 0),
            new EntityStats(2, 4000, 0, 0, 0, 1500, 1500, 1500, 300, 0),
            new EntityStats(3, 8000, 0, 0, 0, 4000, 4000, 4000, 600, 0)
    );
}
