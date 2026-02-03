package GameComponents;

import UtilThings.ResourceType;

import java.util.List;

public abstract class ResourceBuilding extends Building {
    protected List<ResourceWorker> workers;
    protected int workerCapacity;

    ResourceBuilding() {

    }
}
