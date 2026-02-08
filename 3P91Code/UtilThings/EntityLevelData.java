package UtilThings;

import java.util.Arrays;
import java.util.List;

public class EntityLevelData {
    //This is a static class which will hold all the upgrade details
    //All upgradeable entities will have stat lines here (from level 1-?)
    //example of Worker stats:
    //public static final List<EntityStats> WORKER_LEVELS = (new EntityStats(...), ...)


    //Placeholder for now
    //Stat Order:
    //(level, hp, damage, range, productionRate, goldCost, ironCost, lumberCost, timeToCompletion, villageHallReq)
    public static final List<EntityStats> WORKER_LEVELS = Arrays.asList(new EntityStats(1, 100, 100, 5, 5,
            25, 25, 25, 10, 1));
}
