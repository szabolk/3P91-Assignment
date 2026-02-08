package Game;

import GameComponents.*;
import UtilThings.EntityStats;

import java.util.List;

//The village and its components
public class Village {
    private static final int MAX_NUM_BUILDINGS = 10; //for now, idk what max limit should be
    private VillageHall villageHall;
    private List<Building> buildings;
    private List<Inhabitant> inhabitants;
    private Resource resources;
    private Army army;
    private Defences defences;
    private int guardTimeDuration;


    public Village() {

    }

    public void addBuilding(Building building) {

    }

    public void removeBuilding(Building building) {

    }

    public void addInhabitant(Inhabitant inhabitant) {

    }

    public void removeInhabitant(Inhabitant inhabitant) {

    }

    public VillageHall getVillageHall() {
        return null;
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
        return null;
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

    public void upgradeBuilding(Building building) {

    }

    public void upgradeInhabitant(Inhabitant inhabitant) {

    }
}
