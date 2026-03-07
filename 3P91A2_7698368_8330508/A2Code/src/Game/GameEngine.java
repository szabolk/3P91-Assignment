package Game;

import GameComponents.*;
import UtilThings.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

/**
 * Controls what is built, upgraded, trained, etc. Determines when resources are collected and attacks
 * are done on player villages.
 */
public class GameEngine {
    //variables/constants for tracking producing resources
    private long lastProductionTime = 0;
    private static final long TIME_BETWEEN_PRODUCTION = 5000;
    public static final int MAX_NUM_BUILDINGS = 20; //for now, idk what max limit should be
    private Time gameTime;
    private List<Village> villages;


    public GameEngine() {
        this.gameTime = new Time();
        this.villages = new ArrayList<>();
    }

    /**
     * This is used during the creation of the game. Adds the villages of player's to villages list
     * so the engine can interact with them.
     * @param village - A player's village
     */
    public void addVillage(Village village) {
        if (village == null) {
            throw new IllegalArgumentException("Village cannot be null");
        }
        villages.add(village);
    }

    public List<Village> getVillages() {
        return villages;
    }

    public Time getGameTime() {
        return gameTime;
    }

    /**
     * This is used in order to time when resources are to be produced and attacks on player villages
     * should happen. Runs alongside the main UI loop of the game using a separate thread to manage this
     * logic.
     */
    public void run() {
        while (true) {
            long currentTime = gameTime.getTime();
            boolean doProduction = (currentTime - lastProductionTime >= TIME_BETWEEN_PRODUCTION);

            for (Village v : villages) {
                v.doVillageWork(currentTime, doProduction);
                if (currentTime >= v.getGuardedUntil()) {
                    SimulationResult result = simulateAttack(generateVillage(v), v); //no need to add loot to the generated attackers
                    //if the player's village defends, then increment their defense win score, else its counts as a loss
                    GameLogger.log("Player Village Attacked (Guard time fell off)");
                    if (!result.isAttackerWin()) {
                        v.getOwner().addDefenseVictory();
                        GameLogger.log("Player Defence Win - success chance was " + String.format("%.1f%%", 100-result.getSuccessPercentage()));
                    }
                    else {
                        v.getOwner().addDefenseLoss();
                        GameLogger.log("Player Defence Loss - success chance was " + String.format("%.1f%%", 100-result.getSuccessPercentage()));
                    }
                    v.setGuardTime(currentTime); //safe for the next minute -> maybe change later
                }
            }
            //basically if resources got produced, update the times so production will happen again in the correct amount of time
            if (doProduction) {
                lastProductionTime = currentTime;
            }
        }
    }


    /**
     * When the player chooses to explore an attack, it uses this method to generate a suitable
     * village for the player to possibly attack. Also used for when the engine decides that a player
     * is going to be attacked
     *
     * @param playerVillage - the player's village
     * @return Village - returns a suitable village
     */
    public Village generateVillage(Village playerVillage) {
        Random random = new Random();

        int playerAttackScore = Math.max(1, playerVillage.getArmy().getAttackScore()); //just in case player attacks without an army (why in the world would someone do this idk but just in case)
        int playerVillageHallLevel = playerVillage.getVillageHall().getStats().level();

        //choose enemy hall level either one level above or below the player
        int minHall = Math.max(1, playerVillageHallLevel - 1);
        int maxHall = playerVillageHallLevel + 1;
        int enemyHallLevel = random.nextInt(maxHall - minHall + 1) + minHall;

        //generate random resources for the enemy village based on the player village stats
        int maxAmountPerResource = 1000 * playerVillageHallLevel;
        int minAmountPerResource = Math.max(0, maxAmountPerResource / 2); //resources at minimum are 50% of player's
        //randomly determines the amount of each resource the enemy village has baeed on the player's
        int gold = random.nextInt(maxAmountPerResource - minAmountPerResource + 1) + minAmountPerResource;
        int iron = random.nextInt(maxAmountPerResource - minAmountPerResource + 1) + minAmountPerResource;
        int lumber = random.nextInt(maxAmountPerResource - minAmountPerResource + 1) + minAmountPerResource;

        Village enemy = new Village(enemyHallLevel, gold, iron, lumber);

        //make an army similar to players strength
        int targetAttack = Math.max(1, (int) (playerAttackScore * (0.5 + random.nextDouble() * 0.7)));
        int currentAttack = enemy.getArmy().getAttackScore();

        //add basic units until attack score meets or slightly exceeds the players army score
        while (currentAttack < targetAttack) {
            EntityType unit;
            //pick random unit to add to army
            int choice = random.nextInt(4);
            switch (choice) {
                case 0 -> unit = EntityType.SOLDIER;
                case 1 -> unit = EntityType.ARCHER;
                case 2 -> unit = EntityType.KNIGHT;
                default -> unit = EntityType.CATAPULT;
            }
            ArmyUnit newUnit = (ArmyUnit) EntityCreator.createNewInhabitant(unit);
            enemy.getArmy().addUnit(newUnit);
            currentAttack = enemy.getArmy().getAttackScore();
        }

        //make defenses similar to player, same logic as the army creation above
        int targetDefence = Math.max(1, (int) (playerAttackScore * (0.4 + random.nextDouble() * 0.7)));
        int currentDefence = enemy.getDefences().getDefenceScore();

        while (currentDefence < targetDefence) {
            EntityType buildingType;
            int choice = random.nextInt(2);
            if (choice == 0) {
                buildingType = EntityType.ARCHER_TOWER;
            } else {
                buildingType = EntityType.CANNON;
            }
            DefenceBuilding newDefenceBuilding = (DefenceBuilding) EntityCreator.createNewBuilding(buildingType);
            enemy.getDefences().addDefenceBuilding(newDefenceBuilding);
            enemy.getBuildings().add(newDefenceBuilding);
            currentDefence = enemy.getDefences().getDefenceScore();
        }

        return enemy;
    }

    public Army generateArmy(Village playerVillage) {
        return generateVillage(playerVillage).getArmy();
    }

    /**
     * This simply creates a village when a player decides to explore. Once a village is created
     * the village is assigned to the player (see Player.java), so that if the player wishes to attack
     * then they can, otherwise they can reroll the village until they are satisfied.
     * @param playerVillage - A player's village
     * @return - returns a village that is then assigned to the player
     */
    public Village exploreAttack(Village playerVillage) {
        return generateVillage(playerVillage);
    }

    /**
     * Essentially what it does is it takes an attacking army, a villages defences,
     * calculates the attack score (of the army) and the defence score (of the defences)
     * and then calculates the odds and determines if the attackers win the battle.
     * For loot calculations, only a max of 50% reosurces can be taken
     *
     * @param attackingVillage the attacker's village
     * @param defenderVillage the defender's village
     * @return SimulationResult which will be used in a separate method to handle the results
     * (like adding the loot to the attacker's resources, increasing num of wins etc.
     */
    public SimulationResult simulateAttack(Village attackingVillage, Village defenderVillage) {
        Random random = new Random();
        Army attacker = attackingVillage.getArmy();

        //no need for complext attack/defence score calculations as all units have predefined stats that increase with every level
        //so all that needs to be calculated is the relevant stat (attack) to get the scores
        int attackerScore = attacker.getAttackScore();
        int defenderScore = defenderVillage.getDefences().getDefenceScore();

        //apply a multiplier for the attackers and defenders? I assume thats what is meant by "dice rolling" in the pdf
        int diceAttacker = (int) (attackerScore * random.nextDouble(1.0, 2.0));
        int diceDefender = (int) (defenderScore * random.nextDouble(1.0, 2.0));

        //an example of this calculation: winChance = 200 / (200 + 150)) = 0.571 -> 57.1% chance of winning
        double winChance = diceAttacker / (double) (diceAttacker + diceDefender);

        //using the example above, ifthe game produces a roll less than 0.571, then attacker wins
        boolean attackerWin = random.nextDouble() < winChance;

        //determine loot from attack if the attacker won
        GameComponents.Resource loot = null;
        if (attackerWin) {
            GameComponents.Resource defendingVillageResources = defenderVillage.getResources();
            double resourceCap = 0.3; //at most 30% of each resource can be looted
            //if the win chance is less than 50%
            //(meaning that the attacking army was less powerful than the defences and therefore
            //logically would do less destruction to the village) -> should get less loot
            double percentResourceTaken = Math.min(resourceCap, winChance);

            //calculates the amount of loot taken (based on the defending villages resources and the percentage)
            int goldLoot = (int) Math.round(defendingVillageResources.getGold() * percentResourceTaken);
            int ironLoot = (int) Math.round(defendingVillageResources.getIron() * percentResourceTaken);
            int lumberLoot = (int) Math.round(defendingVillageResources.getLumber() * percentResourceTaken);

            //take resources from losing village
            defendingVillageResources.spend(goldLoot, ironLoot, lumberLoot);
            loot = new GameComponents.Resource(defenderVillage, goldLoot, ironLoot, lumberLoot);
        } else {
            //if the attacker lost, they get nothing
            loot = new GameComponents.Resource(defenderVillage, 0, 0, 0);
        }

        return new SimulationResult(attackerWin, loot, winChance * 100.0);
    }

    /**
     * Used when the player decides to attack its explored village, giving an exception if they try to attack a non-existant
     * village
     * @param attackingVillage - player village
     * @param defenderVillage - defending npc village
     * @throws NoVillageExploredException - will throw if the player hasnt explored a village
     */
    public void executeAttack(Village attackingVillage, Village defenderVillage) throws NoVillageExploredException {
        if (defenderVillage == null) {
            throw new NoVillageExploredException("Please explore a village before attacking.");
        }
        simulateAttack(attackingVillage, defenderVillage);
    }

    /**
     * Adds the loot defined in the attack simulation result to the player involved in the attack
     * @param player - player who attacked
     * @param loot - loot stolen in the attack
     */
    public void addLootToPlayer(Player player, Resource loot) {
        player.getVillage().getResources().addResource(ResourceType.GOLD, loot.getGold());
        player.getVillage().getResources().addResource(ResourceType.IRON, loot.getIron());
        player.getVillage().getResources().addResource(ResourceType.LUMBER, loot.getLumber());
    }

    /**
     * Called when the player chooses to build during the game. First, it gets the resource cost for building, and if the player passes all the requirements, then the building or inhabitant
     * is added to the respective queues
     * @param player - the player who wishes to build
     * @param entityToAddType - the type from within the EntityType enum type the player wishes to build or train
     * @throws NotEnoughResourcesException - Will throw if the player doesnt have enough resources
     * @throws MaxBuildingsExceededException - Will throw if the player's amount of buildings have reached the limit
     * @throws QueueFullException - Will throw if the build queue is full. For the building queue, this means that no idle
     * worker exists and therefore cannot build
     */
    public void buildOrTrain(Player player, EntityType entityToAddType) throws NotEnoughResourcesException, MaxBuildingsExceededException, QueueFullException {
        Village village = player.getVillage();
        Resource resources = village.getResources();

        //get the level list for the entity type
        List<EntityStats> levelList = EntityLevelData.getLevels(entityToAddType);
        if (levelList == null) {
            throw new IllegalArgumentException("No level data for entity type: " + entityToAddType);
        }

        //get the gold, iron, and lumber cost listed (see EntityStats for more details on specific costs or stats)
        EntityStats firstLevelStats = levelList.get(0); //index 0 are the level 1 stats
        int goldCost = firstLevelStats.goldCost();
        int ironCost = firstLevelStats.ironCost();
        int lumberCost = firstLevelStats.lumberCost();

        if (!resources.hasEnough(goldCost, ironCost, lumberCost)) {
            throw new NotEnoughResourcesException("Building/Training Failed: Not Enough Resources");
        } else {
            int timeSeconds = firstLevelStats.timeToCompletion();
            long completionTime = gameTime.getTime() + (timeSeconds * 1000L);

            //determine whether the entity to create is an inhabitant or building
            boolean isInhabitant = switch (entityToAddType) {
                case SOLDIER, ARCHER, KNIGHT, CATAPULT,
                     RESOURCE_WORKER, GOLD_MINER, IRON_MINER, LUMBER_MINER,
                     WORKER -> true;
                default -> false;
            };

            if (isInhabitant) {
                // enforce population cap provided by staffed farms
                if ((village.getInhabitants().size() + village.getTrainQueue().size()) >= village.totalPopulationCapacity()) {
                    throw new MaxBuildingsExceededException("Training Failed: Population cap reached");
                }
                resources.spend(goldCost, ironCost, lumberCost);
                village.scheduleTrain(entityToAddType, completionTime);
            } else {
                // treat as a building
                //this stream is used when checking if a new building is able to be built
                //This prevents the players from spamming buildings before they are made (which wouldnt be in the buildings list)
                //which would lead to the getBuildings().size() being incorrect
                long queuedBuildings = village.getBuildQueue().stream()
                        .filter(task -> task.getExistingBuilding() == null)
                        .count();

                if (village.getBuildings().size() + queuedBuildings >= MAX_NUM_BUILDINGS) {
                    throw new MaxBuildingsExceededException("Error: Max Number of Buildings Reached");
                } else if (village.getBuildQueue().size() >= village.workerCount()) {
                    throw new QueueFullException("Queue Full: No Idle Workers");
                }
                resources.spend(goldCost, ironCost, lumberCost);
                village.scheduleBuild(entityToAddType, completionTime);
            }
        }
    }

    /**
     * Whenever a player upgrades, this method will be run. It will first retrieve the new stats to be applied to the entity, and if it passes on the requirements to upgrade,
     * if the entity to be upgraded is an inhabitant, it will be upgraded immediately, but if it is a building it will be put into the build queue (which acts also as an upgrade queue too)
     * @param player - Player that wants to upgrade
     * @param e - The entity the player wants to upgrade
     * @throws NotEnoughResourcesException - Will throw if player doesnt have enough resources
     * @throws UpgradeFailedException - WIll throw if the entity they are trying to upgrade is already max level
     * @throws QueueFullException - WIll throw if the build queue does not have any idle workers
     */
    public void upgrade(Player player, IUpgradeable e) throws NotEnoughResourcesException, UpgradeFailedException, QueueFullException {
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
            throw new UpgradeFailedException("Upgrade Failed: Already at max level");
        }

        //next-level stats are at index currentLevel (Ex. index 0 = level 1, index 1 = level 2, etc.)
        EntityStats nextStats = levelList.get(currentLevel); //Current level is the index of the next level in the list

        if (player.getVillage().getVillageHall().getStats().level() < nextStats.villageHallReq()) {
            throw new UpgradeFailedException("Upgrade Failed: Village Hall is too low level");
        }

        if (!resources.hasEnough(nextStats.goldCost(), nextStats.ironCost(), nextStats.lumberCost())) {
            throw new NotEnoughResourcesException("Upgrade Failed: Not Enough Resources");
        }

        if (e instanceof Inhabitant) {
            resources.spend(nextStats.goldCost(), nextStats.ironCost(), nextStats.lumberCost());
            e.setStats(nextStats); //Upgrades for units are instant, no need to add to queue
            //also for the set stats, the inhabitants that can be attacked (like armyunits) will have their hp refilled on upgrade

        }
        else { //building
            if (village.getBuildQueue().size() >= village.workerCount()) {
                throw new QueueFullException("Queue Full: No Idle Workers");
            }

            resources.spend(nextStats.goldCost(), nextStats.ironCost(), nextStats.lumberCost());

            long completionTime = gameTime.getTime() + (nextStats.timeToCompletion() * 1000L);
            ((Building) e).setUnderConstruction(true); //makes sure that this specific building is not counted in the defence score calculations
            village.scheduleBuildingUpgrade(e.getEntityType(), (Building)e, nextStats, completionTime);
        }
    }

    public static class NotEnoughResourcesException extends Exception {
        public NotEnoughResourcesException(String s) {
            super(s);
        }

        public NotEnoughResourcesException(String s, Throwable cause) {
            super(s, cause);
        }
    }

    public static class UpgradeFailedException extends Exception {
        public UpgradeFailedException(String s) {
            super(s);
        }

        public UpgradeFailedException(String s, Throwable cause) {
            super(s, cause);
        }
    }

    public static class MaxBuildingsExceededException extends Exception {
        public MaxBuildingsExceededException(String s) {
            super(s);
        }

        public MaxBuildingsExceededException(String s, Throwable cause) {
            super(s, cause);
        }
    }

    public static class QueueFullException extends Exception {
        public QueueFullException(String s) {
            super(s);
        }

        public QueueFullException(String s, Throwable cause) {
            super(s, cause);
        }
    }

    public static class NoVillageExploredException extends Exception {
        public NoVillageExploredException(String s) {
            super(s);
        }

        public NoVillageExploredException(String s, Throwable cause) {
            super(s, cause);
        }
    }


}
