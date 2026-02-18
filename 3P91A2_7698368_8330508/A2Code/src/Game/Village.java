package Game;

import GameComponents.*;
import UtilThings.EntityType;
import java.util.List;
import java.util.ArrayList;

import static Game.GameEngine.MAX_NUM_BUILDINGS;

//The village and its components
public class Village {
    private VillageHall villageHall;
    private List<Building> buildings;
    private List<Inhabitant> inhabitants;
    private Resource resources;
    private Army army;
    private Defences defences;
    private int guardTimeDuration;
    private List<QueueTask> buildQueue;
    private List<QueueTask> trainQueue;


    //this constructor will be used for creating the players only
    public Village() {
        this.villageHall = new VillageHall();
        this.buildings = new ArrayList<>(MAX_NUM_BUILDINGS);
        this.inhabitants = new ArrayList<>();
        this.resources = new Resource(this,500, 500, 500);
        this.army = new Army();
        this.defences = new Defences();
        this.guardTimeDuration = 0;
        this.buildQueue = new ArrayList<>();
        this.trainQueue = new ArrayList<>();

        //Starting workers
        for (int i = 0; i < 3; i++) {
            //Adds both mine/mill workers and construction/farm workers
            this.inhabitants.add(new ResourceWorker());
            this.inhabitants.add(new Worker());
        }
    }

    //This will be used when generating villages. Figure out what number to go
    //off (i.e. player level or player offensive strength
    public Village(int level) {

    }

    public static class QueueTask {
        private EntityType type;
        private int completionTime;

        QueueTask(EntityType type, int completionTime) {
            this.type = type;
            this.completionTime = completionTime;
        }

        public EntityType getType() {
            return this.type;
        }

        public int getCompletionTime() {
            return this.completionTime;
        }
    }


    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    public void removeBuilding(Building building) {

    }

    public void addInhabitant(Inhabitant inhabitant) {
        this.inhabitants.add(inhabitant);
    }

    public void removeInhabitant(Inhabitant inhabitant) {

    }

    public VillageHall getVillageHall() {
        return this.villageHall;
    }

    public void setVillageHall(VillageHall hall) {

    }

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public List<Inhabitant> getInhabitants() {
        return this.inhabitants;
    }

    public Resource getResources() {
        return this.resources;
    }

    public Defences getDefences() {
        return this.defences;
    }

    public int idleWorkerCount() {
        return 0;
    }

    public boolean isUnderProtection(Time time) {
        return true;
    }

    public void scheduleBuild(EntityType type, int completionTime) {
        this.buildQueue.add(new QueueTask(type, completionTime));
    }

    public void scheduleTrain(EntityType type, int completionTime) {
        this.trainQueue.add(new QueueTask(type, completionTime));
    }

    public List<QueueTask> getPendingBuilds() {
        return this.buildQueue;
    }

    public List<QueueTask> getPendingTrains() {
        return this.trainQueue;
    }
}
