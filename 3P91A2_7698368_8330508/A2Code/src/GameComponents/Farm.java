package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Farm extends Building {
    private List<Worker> workers;
    private static final int POPULATION_PER_FARM = 5;

    public Farm() {
        this(1);
    }

    public Farm(int level) {
        super(EntityLevelData.FARM_LEVELS.get(level - 1));
        this.workers = new ArrayList<>();
    }

    public boolean addWorker(Worker worker) {
        if (worker == null) {
            return false;
        }
        if (!this.workers.contains(worker) && worker.getAssignedBuilding() == null) {
            this.workers.add(worker);
            worker.setAssignedBuilding(this);
            return true;
        }
        return false;
    }

    public boolean removeWorker(Worker worker) {
        if (worker == null) {
            return false;
        }
        boolean removed = this.workers.remove(worker);
        if (removed && worker.getAssignedBuilding() == this) {
            worker.setAssignedBuilding(null);
        }
        return removed;
    }

    public List<Worker> getWorkers() {
        return this.workers;
    }

    public int supportedPopulation() {
        if (!this.workers.isEmpty()) {
            return POPULATION_PER_FARM;
        }
        return 0;
    }
    @Override
    public EntityType getEntityType() {
        return EntityType.FARM;
    }
}
