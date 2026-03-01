package Game;

import GameComponents.*;
import UtilThings.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
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
     * P.S. What a great and descriptive name lmao - L.S.
     * @param currentTime current game time
     * @param doProduction whether to collect production
     */
    public void doVillageWork(long currentTime, boolean doProduction) {
        if (doProduction) {
            collectAllResources();
        }
        checkBuildTrainQueues(currentTime);
    }

    /**
     * Every certain amount of time, this method will run and add resources to each player's resource pool based on their production rates for each resource
     * This so needs to be refactored (since all 3 mines extend ResourceBuilding and all 3 miners extend ResourceWorker, should be possible to do with wildcards)
     */
    private void collectAllResources() {
        Resource resources = getResources();

        List<GoldMine> goldMines = new ArrayList<>();
        //get the list of all goldmines a player has
        for (Building b : getBuildings()) {
            if (b instanceof GoldMine) {
                goldMines.add((GoldMine)b);
            }
        }
        //calculate the number of gold miners to be counted (ex. If a player has 6 gold miners but only 1 mine, then only 5 of them would be counted)
        int goldCapacity = goldMines.stream()
                                    .mapToInt(b -> ((ResourceBuilding)b).getWorkerCapacity()).sum();

        List<ResourceWorker> goldMiners = new ArrayList<>();

        for (Inhabitant inhabitant : inhabitants) {
            if (inhabitant instanceof GoldMiner) {
                goldMiners.add((ResourceWorker)inhabitant);
            }
        }
        //sort the workers so that the highest level workers (which have a higher production rate) are prioritized over lower level ones
        goldMiners.sort((a,b)->Integer.compare(b.getProductionRate(), a.getProductionRate()));

        int goldCounted = Math.min(goldCapacity, goldMiners.size()); //either holds the max amount of gold miners (based on number of mines), or however many workers there are (ex. 2 mines, but only 6 workers will return 6 instead of 10)
        int goldProduced = 0;
        for (int i = 0; i < goldCounted; i++) {
            goldProduced += goldMiners.get(i).getProductionRate();
        }
        resources.addResource(ResourceType.GOLD, goldProduced);

        List<IronMine> ironMines = new ArrayList<>();
        //get the list of all ironmines a player has
        for (Building b : getBuildings()) {
            if (b instanceof IronMine) {
                ironMines.add((IronMine)b);
            }
        }
        //calculate the number of iron miners to be counted (ex. If a player has 6 iron miners but only 1 mine, then only 5 of them would be counted)
        int ironCapacity = ironMines.stream()
                .mapToInt(b -> ((ResourceBuilding)b).getWorkerCapacity()).sum();

        List<ResourceWorker> ironMiners = new ArrayList<>();

        for (Inhabitant inhabitant : inhabitants) {
            if (inhabitant instanceof IronMiner) {
                ironMiners.add((ResourceWorker)inhabitant);
            }
        }
        //sort the workers so that the highest level workers (which have a higher production rate) are prioritized over lower level ones
        ironMiners.sort((a,b)->Integer.compare(b.getProductionRate(), a.getProductionRate()));

        int ironCounted = Math.min(ironCapacity, ironMiners.size()); //either holds the max amount of iron miners (based on number of mines), or however many workers there are (ex. 2 mines, but only 6 workers will return 6 instead of 10)
        int ironProduced = 0;
        for (int i = 0; i < ironCounted; i++) {
            ironProduced += ironMiners.get(i).getProductionRate();
        }
        resources.addResource(ResourceType.IRON, ironProduced);

        List<LumberMill> lumberMills = new ArrayList<>();
        //get the list of all goldmines a player has
        for (Building b : getBuildings()) {
            if (b instanceof LumberMill) {
                lumberMills.add((LumberMill)b);
            }
        }
        //calculate the number of gold miners to be counted (ex. If a player has 6 gold miners but only 1 mine, then only 5 of them would be counted)
        int lumberCapacity = lumberMills.stream()
                .mapToInt(b -> ((ResourceBuilding)b).getWorkerCapacity()).sum();

        List<ResourceWorker> lumberMiners = new ArrayList<>();

        for (Inhabitant inhabitant : inhabitants) {
            if (inhabitant instanceof LumberMiner) {
                lumberMiners.add((ResourceWorker)inhabitant);
            }
        }
        //sort the workers so that the highest level workers (which have a higher production rate) are prioritized over lower level ones
        lumberMiners.sort((a,b)->Integer.compare(b.getProductionRate(), a.getProductionRate()));

        int lumberCounted = Math.min(lumberCapacity, lumberMiners.size()); //either holds the max amount of gold miners (based on number of mines), or however many workers there are (ex. 2 mines, but only 6 workers will return 6 instead of 10)
        int lumberProduced = 0;
        for (int i = 0; i < lumberCounted; i++) {
            lumberProduced += lumberMiners.get(i).getProductionRate();
        }
        resources.addResource(ResourceType.LUMBER, lumberProduced);
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
