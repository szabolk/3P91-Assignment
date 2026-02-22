package Game;

import GameComponents.*;
import UtilThings.*;
import java.util.Iterator;
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
    private long guardedUntil;
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
        this.guardedUntil = System.currentTimeMillis() + (60 * 1000); //guarded for 60 seconds after creation (may change)
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
    public Village(int playerVillageHalllevel) {
        this.villageHall = new VillageHall(playerVillageHalllevel);
        this.buildings = new ArrayList<>(MAX_NUM_BUILDINGS);
        this.inhabitants = new ArrayList<>();
        this.resources = new Resource(this, 500, 500, 500);
        this.army = new Army();
        this.defences = new Defences();
        this.guardedUntil = 60;
        this.buildQueue = new ArrayList<>();
        this.trainQueue = new ArrayList<>();
    }

    /**
     * Create a village with a specific village hall level and initial resources.
     */
    public Village(int playerVillageHalllevel, int gold, int iron, int lumber) {
        this.villageHall = new VillageHall(playerVillageHalllevel);
        this.buildings = new ArrayList<>();
        this.inhabitants = new ArrayList<>();
        this.resources = new Resource(this, gold, iron, lumber);
        this.army = new Army();
        this.defences = new Defences();
        this.guardedUntil = 60;
        this.buildQueue = new ArrayList<>();
        this.trainQueue = new ArrayList<>();

        //Starting workers (keep same as default)
        for (int i = 0; i < 3; i++) {
            this.inhabitants.add(new ResourceWorker());
            this.inhabitants.add(new Worker());
        }
    }

    public static class QueueTask {
        private final EntityType type;
        private final long completionTime;
        private Building existingBuilding;
        private EntityStats nextStats;

        //Used if building a new building or training a unit
        QueueTask(EntityType type, long completionTime) {
            this.type = type;
            this.completionTime = completionTime;
            this.existingBuilding = null;
        }

        //For upgrading a building
        QueueTask(EntityType type, Building building, EntityStats stats, long completionTime) {
            this.type = type;
            this.existingBuilding = building;
            this.nextStats = stats;
            this.completionTime = completionTime;
        }

        public EntityType getType() {
            return this.type;
        }

        public long getCompletionTime() {
            return this.completionTime;
        }

        public Building getExistingBuilding() {
            return this.existingBuilding;
        }

        public EntityStats getNextStats() {
            return this.nextStats;
        }
    }

    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    public void removeBuilding(Building building) {
        this.buildings.remove(building);
    }

    public void addInhabitant(Inhabitant inhabitant) {
        this.inhabitants.add(inhabitant);
    }

    public void removeInhabitant(Inhabitant inhabitant) {
        this.inhabitants.remove(inhabitant);
    }

    public VillageHall getVillageHall() {
        return this.villageHall;
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

    public Army getArmy() {
        return this.army;
    }

    public Defences getDefences() {
        return this.defences;
    }

    public long getGuardedUntil() {
        return this.guardedUntil;
    }

    /**
     * After a village (player only?) gets attacked, after the attack the village goes into
     * guard mode for a set period of time (1 minute offset from the current time)
     *
     */
    public void setGuardTime() {
        this.guardedUntil = System.currentTimeMillis() + (60 * 1000);
    }
    
    /**
     * This will determine how many buildings can be in the build queue at a single time
     * (since every building needs an idle worker for building/upgrades)
     * Game wise the logic is the same (total workers -> max number of things allowed in the queue at a time)
     * which ensures that every building in the queue has an "idle" worker to work on it
     * @return - number of total workers
     */
    public int workerCount() {
        return (int) inhabitants.stream()
                .filter(inhabitant -> inhabitant instanceof Worker)
                .count();
    }

    /**
     * Count how many workers are assigned to Farm buildings. Determines the max population
     * @return number of workers currently working on farms
     */
    public int workersOnFarmsCount() {
        return (int) inhabitants.stream()
                .filter(inhabitant -> inhabitant instanceof Worker)
                .map(inhabitant -> (Worker) inhabitant)
                .filter(worker -> worker.getAssignedBuilding() instanceof Farm)
                .count();
    }

    /**
     * Gets the total supported population provided by all Farm buildings in this village.
     * @return max population
     */
    public int totalPopulationCapacity() {
        return getBuildings().stream()
                .filter(b -> b instanceof Farm)
                .map(b -> (Farm) b)
                .mapToInt(Farm::supportedPopulation)
                .sum();
    }

    /**
     * collect production, then check build/train queues for completed things
     * P.S. What a great and descriptive name lmao - L.S.
     * @param currentTime current game time
     * @param doProduction whether to collect production
     */
    public void doVillageWork(long currentTime, boolean doProduction) {
        if (doProduction) {
            collectAllResources();
        }
        checkBuildTrainQueues(currentTime);
        //other work
    }

    /**
     * Every certain amount of time, this method will run and add resources to each player's resource pool based on their production rates for each resource
     */
    private void collectAllResources() {
        Resource resources = getResources();
        getBuildings().stream()
                .filter(building -> building instanceof ResourceBuilding)
                .forEach(building -> {
                    ResourceBuilding resourceBuilding = (ResourceBuilding) building;
                    int production = resourceBuilding.production();

                    //determine what type the resource building is and add its production rate to that resource
                    if (resourceBuilding instanceof GoldMine) {
                        resources.addResource(ResourceType.GOLD, production);
                    } else if (resourceBuilding instanceof IronMine) {
                        resources.addResource(ResourceType.IRON, production);
                    } else if (resourceBuilding instanceof LumberMill) {
                        resources.addResource(ResourceType.LUMBER, production);
                    }
                });
    }

    private void checkBuildTrainQueues(long currentTime) {
        Iterator<QueueTask> buildQueueIterator = buildQueue.iterator();
        while (buildQueueIterator.hasNext()) {
            QueueTask currentBuilding = buildQueueIterator.next();
            if (currentBuilding.getCompletionTime() <= currentTime) {
                if (currentBuilding.getExistingBuilding() == null) { //fresh building
                    Building newBuilding = EntityCreator.createNewBuilding(currentBuilding.getType());
                    newBuilding.setUnderConstruction(false);
                    addBuilding(newBuilding);
                }
                else { //this means there is an exisiting building -> means its an upgrade
                    Building upgradedBuilding = currentBuilding.getExistingBuilding();
                    upgradedBuilding.setStats(currentBuilding.getNextStats()); //building's hp will be updated and refilled to max
                    upgradedBuilding.setUnderConstruction(false);
                }
                buildQueueIterator.remove();
            }
        }

        Iterator<QueueTask> trainQueueIterator = trainQueue.iterator();
        while (trainQueueIterator.hasNext()) {
            QueueTask currentInhabitant = trainQueueIterator.next();
            if (currentInhabitant.getCompletionTime() <= currentTime) {
                Inhabitant newInhabitant = EntityCreator.createNewInhabitant(currentInhabitant.getType());
                addInhabitant(newInhabitant);
                trainQueueIterator.remove();
            }
        }
    }


    public void scheduleBuild(EntityType type, long completionTime) {
        this.buildQueue.add(new QueueTask(type, completionTime));
    }

    public void scheduleBuildingUpgrade(EntityType type, Building buildingToUpgrade, EntityStats nextLevelStats, long completionTime) {
        this.buildQueue.add(new QueueTask(type, buildingToUpgrade, nextLevelStats, completionTime));
    }

    public void scheduleTrain(EntityType type, long completionTime) {
        this.trainQueue.add(new QueueTask(type, completionTime));
    }

    public List<QueueTask> getBuildQueue() {
        return this.buildQueue;
    }

    public List<QueueTask> getTrainQueue() {
        return this.trainQueue;
    }
}
