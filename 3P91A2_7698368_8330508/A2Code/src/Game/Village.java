package Game;

import GameComponents.*;
import java.util.List;
import java.util.ArrayList;

//The village and its components
public class Village {
    public static final int MAX_NUM_BUILDINGS = 20; //for now, idk what max limit should be
    private VillageHall villageHall;
    private List<Building> buildings;
    private List<Inhabitant> inhabitants;
    private Resource resources;
    private Army army;
    private Defences defences;
    private int guardTimeDuration;


    //this constructor will be used for creating the players only
    public Village() {
        this.villageHall = new VillageHall();
        this.buildings = new ArrayList<>(MAX_NUM_BUILDINGS);
        this.inhabitants = new ArrayList<>();
        this.resources = new Resource(this,500, 500, 500);
        this.army = new Army();
        this.defences = new Defences();
        this.guardTimeDuration = 0;

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

    public void addBuilding(Building building) throws MaxBuildingsExceededException {
        if (buildings.size() >= MAX_NUM_BUILDINGS) {
            throw new MaxBuildingsExceededException("Error: Max Number of Buildings Reached");
        }
        this.buildings.add(building);
    }

    public void removeBuilding(Building building) {

    }

    public void addInhabitant(Inhabitant inhabitant) {

    }

    public void removeInhabitant(Inhabitant inhabitant) {

    }

    public VillageHall getVillageHall() {
        return this.villageHall;
    }

    public void setVillageHall(VillageHall hall) {

    }

    public List<Building> getBuildings() {
        return null;
    }

    public List<Inhabitant> getInhabitants() {
        return null;
    }

    public Resource getResources() {
        return this.resources;
    }

    public Defences getDefences() {
        return null;
    }

    public void collectResources() {

    }

    public int idleWorkerCount() {
        return 0;
    }

    public boolean isUnderProtection(Time time) {
        return true;
    }

    public void build(Building building) {

    }

    public void train(Inhabitant inhabitant) {

    }

    public class MaxBuildingsExceededException extends Throwable {
        public MaxBuildingsExceededException(String s) {
            super(s);
        }
    }
}
