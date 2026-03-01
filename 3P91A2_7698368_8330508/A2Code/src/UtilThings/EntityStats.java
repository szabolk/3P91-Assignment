package UtilThings;

/**
 * Record makes it so only one line of code essentially represents getters,setters. etc
 * No need for 100+ lines of code just for constructors, getters and setters
 */
public record EntityStats(
        int level,
        int hp,
        int damage,
        int range,
        int productionRate,
        int goldCost,
        int ironCost,
        int lumberCost,
        int timeToCompletion,
        int villageHallReq
) {}
