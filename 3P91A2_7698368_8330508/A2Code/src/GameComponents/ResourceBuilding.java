package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityStats;
import UtilThings.ResourceType;
import java.util.List;

//Will not compile for now (need to create the stats for the constructor) --> See Worker.java for how it will look
public abstract class ResourceBuilding extends Building {
    protected List<ResourceWorker> workers;
    protected int workerCapacity;

    ResourceBuilding(EntityStats stats) {
        super(stats);
    }
}
