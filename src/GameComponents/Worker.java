package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;


public class Worker extends Inhabitant{
    private boolean isIdle;
    private EntityStats stats;

    Worker() {
        //something like this for getting the initial starting stats
        //this.stats = EntityLevelData.WORKER_LEVELS.get(0)
        super(new EntityStats());
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
