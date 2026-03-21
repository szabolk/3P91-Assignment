package UI;

import ChallengeDecision.ChallengeResource;
import ChallengeDecision.ChallengeResult;
import Game.*;
import GameComponents.*;
import UtilThings.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static Game.GameEngine.MAX_NUM_BUILDINGS;

public class UserInterface {
    /**
     * Method which prints the main menu
     */
    public static void printMainMenu() {
        System.out.println("\n---------- Player Menu ----------");
        System.out.println("1. Explore For Attack");
        System.out.println("2. Attack Explored Village");
        System.out.println("3. Build / Train");
        System.out.println("4. Upgrade Building / Unit");
        System.out.println("5. View Village Status");
        System.out.println("6. List All Buildings");
        System.out.println("7. List All Inhabitants");
        System.out.println("8. View Build/Upgrade and Training Queues");
        System.out.println("0. Quit");
        System.out.print("Choice: ");
    }

    /**
     * Prints out the status of a particular village (including its current resources, village hall level, win/loss ratio, etc
     * @param village - particular village we want the stats of
     */
    public static void displayVillageStatus(Village village) {
        System.out.println("\n---------- Village Status ----------");

        System.out.println("Resources:");
        System.out.println("Gold: " + village.getResources().getGold());
        System.out.println("Iron: " + village.getResources().getIron());
        System.out.println("Lumber: " + village.getResources().getLumber());

        System.out.println("\nVillage Hall Level: " + village.getVillageHall().getStats().level());

        System.out.println("\nInhabitants: " + village.getInhabitants().size() + "/" + village.totalPopulationCapacity());

        System.out.println("Buildings: " + village.getBuildings().size() + "/" + MAX_NUM_BUILDINGS);

        System.out.println("Army Units: " + village.getArmy().getUnits().size());
        System.out.println("Attack Score: " + village.getArmy().getAttackScore());

        System.out.println("Defence Buildings: " + village.getDefences().getDefenceBuildings().size());
        System.out.println("Defence Score: " + village.getDefences().getDefenceScore());

        System.out.println("\nPlayer Stats:");
        System.out.println("Attack Wins/Losses: " + village.getOwner().getWinTotal() + " / " + village.getOwner().getLossTotal());
        System.out.println("Defense Wins/Losses: " + village.getOwner().getDefenseVictory() + " / " + village.getOwner().getDefenseLosses());
        System.out.println();
    }

    /**
     * If the player wants to see the build menu and enters it in the previous menu, it will run this.
     * Player can then input a value to tell the game what entity they want built or trained
     * @param player - player who requested the menu
     * @param engine - the game engine
     */
    public static void displayBuildMenu(Player player, GameEngine engine) {
        Scanner buildScanner = new Scanner(System.in);
        boolean buildMenuOpen = true;

        while (buildMenuOpen) {
            System.out.println("\n---------- Build/Train Menu ----------");
            System.out.println("Buildings:");
            System.out.println("1. Farm (50g, 0i, 80l)");
            System.out.println("2. Gold Mine (0g, 50i, 100l)");
            System.out.println("3. Iron Mine (100g, 0i, 100l)");
            System.out.println("4. Lumber Mill (100g, 0i, 0l)");
            System.out.println("5. Archer Tower (100g, 50i, 150l)");
            System.out.println("6. Cannon (200g, 150i, 100l)");
            System.out.println("\nUnits:");
            System.out.println("7. Soldier (60g, 20i, 0l)");
            System.out.println("8. Archer (50g, 0i, 40l)");
            System.out.println("9. Knight (150g, 100i, 0l)");
            System.out.println("10. Catapult (200g, 150i, 200l)");
            System.out.println("11. Worker/Builder (50g, 0i, 10l)");
            System.out.println("12. Gold Miner (60g, 0i, 20l)");
            System.out.println("13. Iron Miner (60g, 0i, 20l)");
            System.out.println("14. Lumber Collector (60g, 0i, 20l)");
            System.out.println("0. Return to main menu");
            System.out.print("Choice: ");

            String buildChoice = buildScanner.nextLine().trim();

            EntityType selectedType = null;
            switch (buildChoice) {
                case "1" -> selectedType = EntityType.FARM;
                case "2" -> selectedType = EntityType.GOLD_MINE;
                case "3" -> selectedType = EntityType.IRON_MINE;
                case "4" -> selectedType = EntityType.LUMBER_MILL;
                case "5" -> selectedType = EntityType.ARCHER_TOWER;
                case "6" -> selectedType = EntityType.CANNON;
                case "7" -> selectedType = EntityType.SOLDIER;
                case "8" -> selectedType = EntityType.ARCHER;
                case "9" -> selectedType = EntityType.KNIGHT;
                case "10" -> selectedType = EntityType.CATAPULT;
                case "11" -> selectedType = EntityType.WORKER;
                case "12" -> selectedType = EntityType.GOLD_MINER;
                case "13" -> selectedType = EntityType.IRON_MINER;
                case "14" -> selectedType = EntityType.LUMBER_MINER;
                case "0" -> buildMenuOpen = false;
                default -> System.out.println("Invalid choice");
            }

            if (selectedType != null) {
                try {
                    engine.buildOrTrain(player, selectedType);
                    System.out.println(selectedType + " scheduled successfully!");
                    GameLogger.log("BUILD/TRAIN queued: " + selectedType);
                } catch (GameEngine.NotEnoughResourcesException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (GameEngine.MaxBuildingsExceededException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (GameEngine.QueueFullException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Prints all buildings in the player's village
     * @param village - village who requested to see all its buildings
     */
    public static void printAllBuildings(Village village) {
        System.out.println("\n---------- Buildings List ----------");
        if (village.getBuildings().isEmpty()) {
            System.out.println("No buildings present.");
            return;
        }
        for (Building b : village.getBuildings()) {
            System.out.printf("%s (lvl %d)%n", b.getEntityType(), b.getStats().level());
        }
    }

    /**
     * Prints all inhabitants in the player's village
     * @param village - village who requested to see all its inhabitants
     */
    public static void printAllInhabitants(Village village) {
        System.out.println("\n---------- Inhabitants List ----------");
        if (village.getInhabitants().isEmpty()) {
            System.out.println("No inhabitants present.");
            return;
        }
        for (Inhabitant i : village.getInhabitants()) {
            System.out.printf("%s (lvl %d)%n", i.getEntityType(), i.getStats().level());
        }
        System.out.println("\n---------- Army Units ----------");
        for (ArmyUnit u : village.getArmy().getUnits()) {
            System.out.printf("%s (lvl %d)%n", u.getEntityType(), u.getStats().level());
        }
    }

    /**
     * Ran if the player chooses to attack a village it has explored
     * @param player - player that wants to attack
     * @param engine - game engine
     */
    public static void attack(Player player, GameEngine engine) {
        ChallengeResult result;
        try {
            result = engine.executeAttack(player.getVillage(), player.getExploredVillage());
        } catch (GameEngine.NoVillageExploredException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (result.getChallengeWon()) {

            System.out.println("Attack Result: Win");
            player.addWin();

            List<ChallengeResource<Double,Double>> loot = result.getLoot();
            int[] resources = new int[3];

            for (int i = 0; i < loot.size() && i < 3; i++) {
                resources[i] = loot.get(i).getProperty().intValue();
            }
            int goldGained = resources[0];
            int ironGained = resources[1];
            int lumberGained = resources[2];

            System.out.println("\nResources Gained:");
            System.out.println("  Gold: +" + goldGained);
            System.out.println("  Iron: +" + ironGained);
            System.out.println("  Lumber: +" + lumberGained);

            //Add loot to player's resources
            engine.addLootToPlayer(player, loot);
            GameLogger.log("Player Attack Win — loot: " + goldGained + "g " + ironGained + "i " + lumberGained + "l");
        }
        else {
            System.out.println("Attack Result: Loss");
            System.out.println("\nNo resources gained.");
            player.addLoss();
            GameLogger.log("Player Attack Loss");
        }

        player.setExploredVillage(null); //after attacking a village, make it so the player cant attack it again
    }

    /**
     * This is used for when a player wants to upgrade. First it lists all upgradeable entities, then the
     * user selects the one they want upgraded.
     * @param upgradeables - the list of entities that can be upgraded
     */
    public static void displayUpgradeMenu(List<IUpgradeable> upgradeables) {
        System.out.println("\n---------- Upgrade Menu ----------");

        if (upgradeables.isEmpty()) {
            System.out.println("Nothing to upgrade.");
            System.out.println("0. Return to main menu");
            System.out.print("Choice: ");
            return;
        }

        int index = 1;
        for (IUpgradeable u : upgradeables) {
            int currentLevel = u.getStats().level();
            List<EntityStats> levels = EntityLevelData.getLevels(u.getEntityType());
            int maxLevel = (levels != null) ? levels.size() : currentLevel;

            if (currentLevel >= maxLevel) {
                System.out.printf("%d. %s (lvl %d / MAX)%n",
                        index, u.getEntityType(), currentLevel);
            } else {
                EntityStats next = levels.get(currentLevel);
                System.out.printf("%d. %s (lvl %d -> %d)  Cost: %dg, %di, %dl%n",
                        index,
                        u.getEntityType(),
                        currentLevel,
                        currentLevel + 1,
                        next.goldCost(),
                        next.ironCost(),
                        next.lumberCost());
            }
            index++;
        }

        System.out.println("0. Return to main menu");
        System.out.print("Choice: ");
    }
    
    /**
     * Displays the village's build and train queues so the player knows when their upgrades will complete
     * @param village - player village
     * @param engine - game engine
     */
    public static void displayQueues(Village village, GameEngine engine) {
        long currentTime = engine.getGameTime().getTime();

        //build/upgrade Queue
        System.out.println("\n---------- Build / Upgrade Queue ----------");
        List<Village.QueueTask> buildQueue = village.getBuildQueue();
        if (buildQueue.isEmpty()) {
            System.out.println("Build/Upgrade Queue is Empty");
        } else {
            int index = 1;
            for (Village.QueueTask task : buildQueue) {
                long secondsRemaining = Math.max(0, (task.getCompletionTime() - currentTime) / 1000);
                boolean isUpgrade = task.getExistingBuilding() != null;
                String action = isUpgrade ? "Upgrading" : "Building";
                System.out.printf("%d. %s %s  -  %ds remaining%n", index++, action, task.getType(), secondsRemaining);
            }
        }

        //training Queue
        System.out.println("\n---------- Training Queue ----------");
        List<Village.QueueTask> trainQueue = village.getTrainQueue();
        if (trainQueue.isEmpty()) {
            System.out.println("Training Queue is Empty");
        } else {
            int index = 1;
            for (Village.QueueTask task : trainQueue) {
                long secondsRemaining = Math.max(0, (task.getCompletionTime() - currentTime) / 1000);
                System.out.printf("%d. Training %s  -  %ds remaining%n", index++, task.getType(), secondsRemaining);
            }
        }
    }

    /**
     * Whenever the player explores, this information about the explored village will appear in the terminal
     * @param village - player village
     */
    public static void displayExploredVillage(Village village) {
        System.out.println("\n---------- Explored Village ----------");
        System.out.println("Village Hall Level: " + village.getVillageHall().getStats().level());

        System.out.println("\nResources:");
        System.out.println("Gold: " + village.getResources().getGold());
        System.out.println("Iron: " + village.getResources().getIron());
        System.out.println("Lumber: " + village.getResources().getLumber());

        System.out.println("\nAttack Power: " + village.getArmy().getAttackScore());
        System.out.println("Defence Power: " + village.getDefences().getDefenceScore());
    }

    /**
     * Displays the result returned from the ChallengeResult (in game engine)
     * @param goldGained - gold gained
     * @param ironGained - iron gained
     * @param lumberGained - lumber gained
     */
    public static void displayAttackWin(int goldGained, int ironGained, int lumberGained) {
        System.out.println("Attack Result: Win");
        System.out.println("\nResources Gained:");
        System.out.println("Gold: +" + goldGained);
        System.out.println("Iron: +" + ironGained);
        System.out.println("Lumber: +" + lumberGained);
    }

    /**
     * Tells the user they lost the attack
     */
    public static void displayAttackLoss() {
        System.out.println("Attack Result: Loss");
        System.out.println("\nNo resources gained.");
    }

    public static void printMessage(String msg) {
        System.out.println(msg);
    }

    public static void printBuildMenu() {
        System.out.println("\n---------- Build/Train Menu ----------");
        System.out.println("Buildings:");
        System.out.println("1. Farm (50g, 0i, 80l)");
        System.out.println("2. Gold Mine (0g, 50i, 100l)");
        System.out.println("3. Iron Mine (100g, 0i, 100l)");
        System.out.println("4. Lumber Mill (100g, 0i, 0l)");
        System.out.println("5. Archer Tower (100g, 50i, 150l)");
        System.out.println("6. Cannon (200g, 150i, 100l)");
        System.out.println("\nUnits:");
        System.out.println("7. Soldier (60g, 20i, 0l)");
        System.out.println("8. Archer (50g, 0i, 40l)");
        System.out.println("9. Knight (150g, 100i, 0l)");
        System.out.println("10. Catapult (200g, 150i, 200l)");
        System.out.println("11. Worker/Builder (50g, 0i, 10l)");
        System.out.println("12. Gold Miner (60g, 0i, 20l)");
        System.out.println("13. Iron Miner (60g, 0i, 20l)");
        System.out.println("14. Lumber Collector (60g, 0i, 20l)");
        System.out.println("0. Return to main menu");
        System.out.print("Choice: ");
    }
}
