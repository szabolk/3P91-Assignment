package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Worker extends Inhabitant{
    private boolean isIdle;

    public Worker() {
        this(1);
    }

    public Worker(int level) {
        super(EntityLevelData.WORKER_LEVELS.get(level - 1)); //(level - 1) is the first index in the WORKERS_LEVEL list aka level 1
        this.isIdle = true;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WORKER;
    }

    public boolean getIsIdle() {
        return this.isIdle;
    }
    public void toggleIsIdle() {
        this.isIdle = !this.isIdle;
    }
    public void setIsIdle(boolean status) {
        this.isIdle = status;
    }
}
