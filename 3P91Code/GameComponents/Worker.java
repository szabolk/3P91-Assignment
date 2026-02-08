package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityStats;
import UtilThings.EntityType;


public class Worker extends Inhabitant{
    private boolean isIdle;
    private EntityStats stats;

    Worker() {
        //something like this for getting the initial starting stats
        super(new EntityStats(EntityLevelData.WORKER_LEVELS.get(0)));
        this.isIdle = true;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WORKER;
    }

    public boolean getIdleStatus() {
        return this.isIdle;
    }
}
