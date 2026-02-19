package Game;

import GameComponents.*;
import UtilThings.*;

import java.util.ArrayList;
import java.util.List;

//Controls the game (maybe do a canUpdate/canTrain/canBuild here as well?)
public class GameEngine {

    //variables/constants for tracking producing resources
    private long lastProductionTime = 0;
    private static final long TIME_BETWEEN_PRODUCTION = 5000;
    private static final long TIME_BETWEEN_ATTACKS = 10000; //used for calculating if an attack can happen
    public static final int MAX_NUM_BUILDINGS = 20; //for now, idk what max limit should be

    private Time gameTime;
    private List<Village> villages;


    public GameEngine() {
        this.gameTime = new Time();
        this.villages = new ArrayList<>();
    }

    public Time getGameTime() {
        return gameTime;
    }

    /**
     * Should be the game loop
     */
    public void runGame() {
        while (true) {
            long currentTime = gameTime.getTime();

            //check if the production happens and add the resources to each player's resource pool
            if (currentTime - lastProductionTime >= TIME_BETWEEN_PRODUCTION) {
                collectAllResources();
                lastProductionTime = currentTime;
            }
            // process any builds or training that have completed
            checkBuildTrainQueues();
            //check if attack is to happen against the players and if they are not in guard time
            //check if buildings are finished building or upgrades are done
        }
    }

    /**
     * Every certain amount of time, this method will run and add resources to each player's resource pool based on their production rates for each resource
     */
    private void collectAllResources() {
        villages.forEach(village -> {
            Resource resources = village.getResources();
            village.getBuildings().stream()
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
        });
    }


    /**
     * When the player chooses to explore an attack, it uses this method to generate a suitable
     * village for the player to possibly attack
     *
     * @param playerVillage - the player's village
     * @return Village - returns a suitable village
     */
    public Village generateVillage(Village playerVillage) {
        return null;
    }

    /**
     * Essentially what it does is it takes an attacking army, a villages defences,
     * calculates the attack score (of the army) and the defence score (of the defences)
     * and then calculates the odds and determines if the attackers win the battle
     *
     * @param attacker the attacker's army
     * @param defender the defender's defences
     * @return SimulationResult which will be used in a separate method to handle the results
     * (like adding the loot to the attacker's resources, increasing num of wins etc.
     */
    public SimulationResult simulateAttack(Army attacker, Defences defender) {
        return null;
    }


    /**
     * After a village (player only?) gets attacked, after the attack the village goes into
     * guard mode for a set period of time (offset from the current time)
     *
     * @param village - village that was attacked
     */
    public void setGuardTime(Village village) {

    }

    public void buildOrTrain(Player player, Entity e) throws NotEnoughResourcesException, MaxBuildingsExceededException, QueueFullException {
        Village village = player.getVillage();
        Resource resources = village.getResources();
        EntityStats stats = e.getStats();

        //get the level list for the entity
        List<EntityStats> levelList = EntityLevelData.getLevels(e.getEntityType());
        if (levelList == null) {
            throw new IllegalArgumentException("No level data for entity type: " + e.getEntityType());
        }

        EntityStats firstLevelStats = levelList.get(0); //index 0 are the level 1 stats

        int goldCost = firstLevelStats.goldCost();
        int ironCost = firstLevelStats.ironCost();
        int lumberCost = firstLevelStats.lumberCost();

        if (!resources.hasEnough(goldCost, ironCost, lumberCost)) {
            throw new NotEnoughResourcesException("Building/Training Failed: Not Enough Resources");
        } else {
            EntityType entityToAddType = e.getEntityType();
            int timeSeconds = firstLevelStats.timeToCompletion();
            int completionTime = gameTime.getTime() + (timeSeconds * 1000);
            //different mechanisms for both as the process for their creation is somewhat different
            if (e instanceof Inhabitant) {
                resources.spend(goldCost, ironCost, lumberCost);
                village.scheduleTrain(entityToAddType, completionTime);
            }
            else if (e instanceof Building) {
                if (village.getBuildings().size() >= MAX_NUM_BUILDINGS) {
                    throw new MaxBuildingsExceededException("Error: Max Number of Buildings Reached");
                }
                else if (village.getBuildQueue().size() >= village.workerCount()) {
                    throw new QueueFullException("Queue Full: No Idle Workers");
                }
                resources.spend(goldCost, ironCost, lumberCost);
                village.scheduleBuild(entityToAddType, completionTime);
            }
        }
    }

    private void checkBuildTrainQueues() {
        //for each village, this will go through the build and train queues
        //if the completion time of something is less than the currentTime, than that
        //thing is done, it can be removed from the queue, and the entity can be created and
        //added to that village's building or inhabitant queue
    }

    public void upgrade(Player player, IUpgradeable e) throws NotEnoughResourcesException, MaxLevelException, QueueFullException {
        Village village = player.getVillage();
        Resource resources = village.getResources();
        //get the level list for the entity
        List<EntityStats> levelList = EntityLevelData.getLevels(e.getEntityType());

        if (levelList == null) {
            throw new IllegalArgumentException("No level data for entity type: " + e.getEntityType());
        }

        int currentLevel = e.getStats().level();
        int maxLevel = levelList.size(); //uses the size of the level lists to determine how many levels a unit has (Ex. a soldier has 3 stat lines so 3 levels, village hall has 6 statlines -> 6 levels)

        if (currentLevel >= maxLevel) {
            throw new MaxLevelException("Upgrade Failed: Already at max level");
        }

        //next-level stats are at index currentLevel (Ex. index 0 = level 1, index 1 = level 2, etc.)
        EntityStats nextStats = levelList.get(currentLevel); //Current level is the index of the next level in the list

        if (!resources.hasEnough(nextStats.goldCost(), nextStats.ironCost(), nextStats.lumberCost())) {
            throw new NotEnoughResourcesException("Upgrade Failed: Not Enough Resources");
        }

        if (e instanceof Inhabitant) {
            resources.spend(nextStats.goldCost(), nextStats.ironCost(), nextStats.lumberCost());
            e.setStats(nextStats); //Upgrades for units are instant, no need to add to queue
        }
        else if (e instanceof Building) {
            if (village.getBuildQueue().size() >= village.workerCount()) {
                throw new QueueFullException("Queue Full: No Idle Workers");
            }

            resources.spend(nextStats.goldCost(), nextStats.ironCost(), nextStats.lumberCost());

            int completionTime = gameTime.getTime() + (nextStats.timeToCompletion() * 1000);
            village.scheduleBuildingUpgrade(e.getEntityType(), (Building)e, nextStats, completionTime);
        }
    }

    public class NotEnoughResourcesException extends Exception {
        public NotEnoughResourcesException(String s) {
            super(s);
        }
    }

    public class MaxLevelException extends Exception {
        public MaxLevelException(String s) {
            super(s);
        }
    }

    public class MaxBuildingsExceededException extends Exception {
        public MaxBuildingsExceededException(String s) {
            super(s);
        }
    }

    public class QueueFullException extends Exception {
        public QueueFullException(String s) {
            super(s);
        }
    }


}
