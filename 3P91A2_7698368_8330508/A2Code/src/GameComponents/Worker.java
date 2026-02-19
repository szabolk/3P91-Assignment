package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Worker extends Inhabitant{
    public Worker() {
        this(1);
    }

    public Worker(int level) {
        super(EntityLevelData.WORKER_LEVELS.get(level - 1)); //(level - 1) is the first index in the WORKERS_LEVEL list aka level 1
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WORKER;
    }
}
