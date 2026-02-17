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

    public void build(Player player, Building building) {
        Village village = player.getVillage();
        Resource resources = village.getResources();
        EntityStats costs = building.getStats();
        //this is going to be a pain in the ass
        //check if player has enough resources
        //check if they do not already have the max number of buildings
        //
    }

    public void train(Player player, Inhabitant inhabitant) {
        Village village = player.getVillage();
        Resource resources = village.getResources();
        EntityStats stats = inhabitant.getStats();
    }

    public void upgrade(Player player, IUpgradeable e) throws NotEnoughResourcesException, MaxLevelException {
        Village village = player.getVillage();
        Resource resources = village.getResources();
        EntityStats stats = e.getStats();

        //get the level list for the entity
        List<EntityStats> levelList = EntityLevelData.getLevels(e.getEntityType());
        if (levelList == null) {
            throw new IllegalArgumentException("No level data for entity type: " + e.getEntityType());
        }

        int currentLevel = stats.level();
        int maxLevel = levelList.size(); //uses the size of the level lists to determine how many levels a unit has (Ex. a soldier has 3 stat lines so 3 levels, village hall has 6 statlines -> 6 levels)

        if (currentLevel >= maxLevel) {
            throw new MaxLevelException("Upgrade Failed: Already at max level");
        }

        //next-level stats are at index currentLevel (Ex. index 0 = level 1, index 1 = level 2, etc.)
        EntityStats nextStats = levelList.get(currentLevel); //Current level is the index of the next level in the list

        int goldCost = nextStats.goldCost();
        int ironCost = nextStats.ironCost();
        int lumberCost = nextStats.lumberCost();

        if (!resources.hasEnough(goldCost, ironCost, lumberCost)) {
            throw new NotEnoughResourcesException("Upgrade Failed: Not Enough Resources");
        } else {
            //subtract resources and apply the upgrade
            resources.spend(goldCost, ironCost, lumberCost);
            e.setStats(nextStats);
        }


    }

    public class NotEnoughResourcesException extends RuntimeException {
        public NotEnoughResourcesException(String s) {
            super(s);
        }
    }

    public class MaxLevelException extends RuntimeException {
        public MaxLevelException(String s) {
            super(s);
        }
    }
}
