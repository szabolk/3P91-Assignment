package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityStats;
import UtilThings.EntityType;

import java.util.List;

//Will not compile for now (need to create the stats for the constructor) --> See Worker.java for how it will look
public class Farm extends Building {
    private List<Worker> workers;

    public Farm() {
        super(new EntityStats(EntityLevelData.WORKER_LEVELS.get(0)));
    }

    @Override
    public EntityType getEntityType() {
        return null;
    }
}
