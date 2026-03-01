package Game;

import GameComponents.*;
import UtilThings.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static Game.GameEngine.MAX_NUM_BUILDINGS;

/**
 * This class contains all information relevant to a village, like its buildings, inhabitants,
 * army, defences, village hall. Keeps tracks of what entities are being built, upgraded, trained, etc.
 */
public class Village {
    private Player owner;
    private VillageHall villageHall;
    private List<Building> buildings;
    private List<Inhabitant> inhabitants;
    private Resource resources;
    private Army army;
    private Defences defences;
    private long guardedUntil;
    private List<QueueTask> buildQueue;
    private List<QueueTask> trainQueue;


    /**
     * This constructor is used specifically when creating a player's village
     */
    public Village(Player owner) {
        this.owner = owner;
        this.villageHall = new VillageHall();
        this.buildings = new ArrayList<>(MAX_NUM_BUILDINGS);
        this.inhabitants = new ArrayList<>();
        this.resources = new Resource(this,500, 500, 500);
        this.army = new Army();
        this.defences = new Defences();
        this.guardedUntil = 60000; //guarded for 60 seconds after game start
        this.buildQueue = new ArrayList<>();
        this.trainQueue = new ArrayList<>();

        //Starting workers
        this.inhabitants.add(new GoldMiner());
        this.inhabitants.add(new IronMiner());
        this.inhabitants.add(new LumberMiner());
        this.inhabitants.add(new Worker());
        this.inhabitants.add(new Worker());
        
        //Starting army units so player doesn't get steamrolled
        this.army.addUnit(new Soldier());
        this.army.addUnit(new Archer());
        this.army.addUnit(new Catapult());
        
        //Starting defense buildings and one of each resource building so the player just cant go bankrupt if they forget to build a specific type
        ArcherTower tower1 = new ArcherTower();
        ArcherTower tower2 = new ArcherTower();
        this.defences.addDefenceBuilding(tower1);
        this.defences.addDefenceBuilding(tower2);
        this.buildings.add(tower1);
        this.buildings.add(tower2);
        this.buildings.add(new GoldMine());
        this.buildings.add(new IronMine());
        this.buildings.add(new LumberMill());
        // add a farm so there is initial population capacity for the starting inhabitants
        this.buildings.add(new Farm());
    }

    /**
     * This constructor is used specifically when creating a village to attack the player's village
     */
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
     * This constructor is used specifically when creating a village for the player to attack
     */
    public Village(int playerVillageHalllevel, int gold, int iron, int lumber) {
        this.villageHall = new VillageHall(playerVillageHalllevel);
        this.buildings = new ArrayList<>();
        this.inhabitants = new ArrayList<>();
        this.resources = new Resource(this, gold, iron, lumber);
        this.army = new Army();
        this.defences = new Defences();
        this.buildQueue = new ArrayList<>();
        this.trainQueue = new ArrayList<>();
    }

    /**
     * This class defines how tasks within the build/train are structured
     */
    public static class QueueTask {
        private final EntityType type;
        private final long completionTime;
        private Building existingBuilding;
        private EntityStats nextStats;

        //Used if building a new building or training a unit
        public QueueTask(EntityType type, long completionTime) {
            this.type = type;
            this.completionTime = completionTime;
            this.existingBuilding = null;
        }

        //For upgrading a building
        public QueueTask(EntityType type, Building building, EntityStats stats, long completionTime) {
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

    public Player getOwner() {
        return owner;
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
     * After a village (player only) gets attacked, after the attack the village goes into
     * guard mode for a set period of time (1 minute offset from the current game time)
     *
     */
    public void setGuardTime(long currentGameTime) {
        this.guardedUntil = currentGameTime + (60 * 1000);
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
     * @param currentTime current game time
     * @param doProduction whether to collect production
     */
    public void doVillageWork(long currentTime, boolean doProduction) {
        if (doProduction) {
            collectAllResources();
        }
        checkBuildTrainQueues(currentTime);
    }

    public void collectAllResources() {
        collectResource(GoldMine.class, GoldMiner.class, ResourceType.GOLD);
        collectResource(IronMine.class, IronMiner.class, ResourceType.IRON);
        collectResource(LumberMill.class, LumberMiner.class, ResourceType.LUMBER);
    }

    /**
     * Method called only by collectAllResources(). The way it works is as followed: First, the game determines how many workers can possibly be working on each resource (based on the sum of
     * capacity of the same resource building i.e. 2 mines -> 10 workers allowed to work. Second, it only considers up to N workers, where N is the total capacity, with workers that have a
     * higher production rate being favoured over low production rate workers. Finally, it calculates the total production for that resource this resource production cycle and adds it to the player's
     * resource pool
     * @param mineType - the type of resource building
     * @param workerType - the type of worker
     * @param resource - the resource to be added
     */
    private void collectResource(Class<? extends ResourceBuilding> mineType, Class<? extends ResourceWorker> workerType, ResourceType resource) {
        int capacity = buildings.stream()
                .filter(mineType::isInstance)
                .map(mineType::cast)
                .mapToInt(ResourceBuilding::getWorkerCapacity)
                .sum();

        //creates a list of workers that work on a specific resource type
        List<ResourceWorker> workers = inhabitants.stream()
                .filter(workerType::isInstance)
                .map(workerType::cast)
                .collect(Collectors.toList());

        //sorts the workers in descending order of production rate so that the highest production has more priority
        workers.sort(Comparator.comparingInt(ResourceWorker::getProductionRate).reversed());

        //only uses worker production based on the number of buildings and how many workers COULD physically be working (based on the limits of the resource buildings)
        //Ex. If the player has only 1 gold mine (with a worker limit of 5) and 6 workers, then only the highest 5 production workers will be counted
        int produced = workers.stream()
                .limit(capacity)
                .mapToInt(ResourceWorker::getProductionRate)
                .sum();

        this.resources.addResource(resource, produced);
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


    /**
     * This method is used whenever a build instruction is given
     * @param type - type of building the player wants
     * @param completionTime - time the task will finish at
     */
    public void scheduleBuild(EntityType type, long completionTime) {
        this.buildQueue.add(new QueueTask(type, completionTime));
    }

    /**
     * This method is used whenever the player wants to upgrade an existing building
     * @param type - the type of building so it knows what stats to grab
     * @param buildingToUpgrade - the actual building object
     * @param nextLevelStats - the next level stats from EntityStats
     * @param completionTime - time the task will finish at
     */
    public void scheduleBuildingUpgrade(EntityType type, Building buildingToUpgrade, EntityStats nextLevelStats, long completionTime) {
        this.buildQueue.add(new QueueTask(type, buildingToUpgrade, nextLevelStats, completionTime));
    }

    /**
     * Used when a player wants to train a new unit
     * @param type - type of inhabitant the user wants
     * @param completionTime - time the task will finish at
     */
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
