package GameComponents;

import UtilThings.EntityStats;

import java.util.ArrayList;
import java.util.List;

public abstract class ResourceBuilding extends Building {
    protected List<ResourceWorker> workers;
    protected int workerCapacity;

    ResourceBuilding(EntityStats stats) {
        super(stats);
        this.workerCapacity = 1 + stats.level();
        this.workers = new ArrayList<>(this.workerCapacity);
    }

    public int getWorkerCapacity() {
        return this.workerCapacity;
    }

    public int production() {
        int totalProduction = 0;
        for (ResourceWorker worker : workers) {
            totalProduction += worker.getProductionRate();
        }
        return totalProduction;
    }
}
