package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Worker extends Inhabitant{
    private Building assignedBuilding;

    public Worker() {
        this(1);
    }

    public Worker(int level) {
        super(EntityLevelData.WORKER_LEVELS.get(level - 1)); //(level - 1) is the first index in the WORKERS_LEVEL list aka level 1
    }

    public Building getAssignedBuilding() {
        return this.assignedBuilding;
    }

    public void setAssignedBuilding(Building building) {
        this.assignedBuilding = building;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WORKER;
    }
}
