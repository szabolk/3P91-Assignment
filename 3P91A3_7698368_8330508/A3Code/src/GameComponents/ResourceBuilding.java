package GameComponents;

import UtilThings.EntityStats;

/**
 * Logic around resource buildings is done here
 */
public abstract class ResourceBuilding extends Building {
    protected int workerCapacity;

    ResourceBuilding(EntityStats stats) {
        super(stats);
        this.workerCapacity = 1 + stats.level();
    }

    public int getWorkerCapacity() {
        return this.workerCapacity;
    }
}
