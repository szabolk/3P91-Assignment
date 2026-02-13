package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityStats;
import UtilThings.EntityType;

import java.util.List;

//Will not compile for now (need to create the stats for the constructor) --> See Worker.java for how it will look
public class Farm extends Building {
    private List<Worker> workers;

    public Farm() {
        this(1);
    }

    public Farm(int level) {
        super(EntityLevelData.WORKER_LEVELS.get(level - 1));
    }
    @Override
    public EntityType getEntityType() {
        return null;
    }
}
