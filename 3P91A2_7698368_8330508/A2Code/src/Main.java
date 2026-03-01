import Game.*;
import GameComponents.*;
import Game.Army;
import UtilThings.EntityStats;
import UtilThings.EntityLevelData;
import UtilThings.EntityType;
import UtilThings.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static Game.GameEngine.MAX_NUM_BUILDINGS;

public class Main {
    public static void main(String[] args) {
        //idk if this goes in the UI class or here is fine
        GameEngine engine = new GameEngine();
        Player player = new Player();
        engine.addVillage(player.getVillage());

        //this is used for the real time
        Thread gameThread = new Thread(() -> engine.run());
        gameThread.setDaemon(true);
        gameThread.start();
        //just the skeleton for now, needs to be worked on
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            printMainMenu();
            String line = scanner.nextLine().trim();
            switch (line) {
                case "1" -> explore(player, engine);
                case "2" -> attack(player, engine);
                case "3" -> displayBuildMenu(player, engine);
                case "4" -> displayUpgradeMenu(player, engine);
                case "5" -> displayVillageStatus(player.getVillage());
                case "6" -> printAllBuildings(player.getVillage());
                case "7" -> printAllInhabitants(player.getVillage());
                case "8" -> displayQueues(player.getVillage(), engine);
                case "0" -> isRunning = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }


    //maybe add to see what things are in the build / train queue, think of other qol things maybe? WOOOO ITS 11pm
    private static void printMainMenu() {
        System.out.println("\n=== Player Menu ===");
        System.out.println("1. Explore for attack");
        System.out.println("2. Attack Explored Village");
        System.out.println("3. Build / Train");
        System.out.println("4. Upgrade Building / Unit");
        System.out.println("5. View village status");
        System.out.println("6. List all buildings");
        System.out.println("7. List all inhabitants");
        System.out.println("8. View build/upgrade and training queues");
        System.out.println("0. Quit");
        System.out.print("Choice: ");
    }

    private static void displayVillageStatus(Village village) {
        System.out.println("\n=== Village Status ===");

        System.out.println("Resources:");
        System.out.println("  Gold: " + village.getResources().getGold());
        System.out.println("  Iron: " + village.getResources().getIron());
        System.out.println("  Lumber: " + village.getResources().getLumber());

        System.out.println("\nVillage Hall Level: " + village.getVillageHall().getStats().level());

        System.out.println("\nInhabitants: " + village.getInhabitants().size() + "/" + village.totalPopulationCapacity());

        System.out.println("Buildings: " + village.getBuildings().size() + "/" + MAX_NUM_BUILDINGS);

        System.out.println("Army Units: " + village.getArmy().getUnits().size());

        System.out.println("Defence Buildings: " + village.getDefences().getDefenceBuildings().size());
        
        System.out.println("\nPlayer Stats:");
        System.out.println("Attack Wins/Losses: " + village.getOwner().getWinTotal() + " / " + village.getOwner().getLossTotal());
        System.out.println("Defense Wins/Losses: " + village.getOwner().getDefenseVictory() + " / " + village.getOwner().getDefenseLosses());
        System.out.println();
    }

    private static void displayBuildMenu(Player player, GameEngine engine) {
        Village village = player.getVillage();
        Scanner buildScanner = new Scanner(System.in);
        boolean buildMenuOpen = true;

        while (buildMenuOpen) {
            System.out.println("\n=== Build / Train Menu ===");
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

    private static void printAllBuildings(Village village) {
        System.out.println("\n=== Buildings List ===");
        if (village.getBuildings().isEmpty()) {
            System.out.println("No buildings present.");
            return;
        }
        for (Building b : village.getBuildings()) {
            System.out.printf("%s (lvl %d)%n", b.getEntityType(), b.getStats().level());
        }
    }

    private static void printAllInhabitants(Village village) {
        System.out.println("\n=== Inhabitants List ===");
        if (village.getInhabitants().isEmpty()) {
            System.out.println("No inhabitants present.");
            return;
        }
        for (Inhabitant i : village.getInhabitants()) {
            System.out.printf("%s (lvl %d)%n", i.getEntityType(), i.getStats().level());
        }
        System.out.println("\n=== Army Units ===");
        for (ArmyUnit u : village.getArmy().getUnits()) {
            System.out.printf("%s (lvl %d)%n", u.getEntityType(), u.getStats().level());
        }
    }

    private static void attack(Player player, GameEngine engine) {
        if (player.getExploredVillage() == null) {
            System.out.println("Could Not Attack: No Village Explored Yet");
            return;
        }
        SimulationResult result = engine.simulateAttack(player.getVillage(), player.getExploredVillage());
        if (result.isAttackerWin()) {
            System.out.println("Attack Result: Win");
            player.addWin();

            Resource loot = result.getLoot();
            System.out.println("\nResources Gained:");
            System.out.println("  Gold: +" + loot.getGold());
            System.out.println("  Iron: +" + loot.getIron());
            System.out.println("  Lumber: +" + loot.getLumber());

            //Add loot to player's resources
            player.getVillage().getResources().addResource(ResourceType.GOLD, loot.getGold());
            player.getVillage().getResources().addResource(ResourceType.IRON, loot.getIron());
            player.getVillage().getResources().addResource(ResourceType.LUMBER, loot.getLumber());

        }
        else {
            System.out.println("Attack Result: Loss");
            System.out.println("\nNo resources gained.");
            player.addLoss();
        }
        System.out.printf("Attack success chance was %.1f%%", result.getSuccessPercentage());
        player.setExploredVillage(null); //after attacking a village, make it so the player cant attack it again
    }

    private static void explore(Player player, GameEngine engine) {
        Village exploredVillage = engine.exploreAttack(player.getVillage());
        player.setExploredVillage(exploredVillage);

        System.out.println("\n=== Explored Village ===");

        System.out.println("Village Hall Level: " + exploredVillage.getVillageHall().getStats().level());

        System.out.println("\nResources:");
        System.out.println("  Gold: " + exploredVillage.getResources().getGold());
        System.out.println("  Iron: " + exploredVillage.getResources().getIron());
        System.out.println("  Lumber: " + exploredVillage.getResources().getLumber());

        System.out.println("\nAttack Power: " + exploredVillage.getArmy().getAttackScore());
        System.out.println("Defense Power: " + exploredVillage.getDefences().getDefenceScore());
    }

    private static void displayUpgradeMenu(Player player, GameEngine engine) {
        Village village = player.getVillage();
        Scanner upgradeScanner = new Scanner(System.in);

        //combined list of everything upgradeable
        List<IUpgradeable> upgradeables = new ArrayList<>();

        upgradeables.add(village.getVillageHall());

        for (Building b : village.getBuildings()) {
            upgradeables.add(b);
        }
        for (Inhabitant i : village.getInhabitants()) {
            upgradeables.add(i);
        }
        for (ArmyUnit u : village.getArmy().getUnits()) {
            upgradeables.add(u);
        }

        System.out.println("\n=== Upgrade Menu ===");
        if (upgradeables.isEmpty()) {
            System.out.println("Nothing to upgrade.");
            return;
        }

        int index = 1; //will be used for selecting the entity the palyers wants to upgrade
        for (IUpgradeable u : upgradeables) {
            int currentLevel = u.getStats().level();
            List<EntityStats> levels = EntityLevelData.getLevels(u.getEntityType());

            int maxLevel;
            if (levels != null) {
                maxLevel = levels.size();
            }
            else {
                maxLevel = currentLevel;
            }

            if (currentLevel >= maxLevel) {
                //cannot upgrade any further
                System.out.printf("%d. %s (lvl %d / MAX)%n", index, u.getEntityType(), currentLevel);
            } else {
                EntityStats nextStats = levels.get(currentLevel); //index = next level
                System.out.printf("%d. %s (lvl %d -> %d)  Cost: %dg, %di, %dl%n",
                        index,
                        u.getEntityType(),
                        currentLevel,
                        currentLevel + 1,
                        nextStats.goldCost(),
                        nextStats.ironCost(),
                        nextStats.lumberCost()
                );
            }
            index++;
        }

        System.out.println("0. Return to main menu");
        System.out.print("Choice: ");

        String input = upgradeScanner.nextLine().trim();

        if (input.equals("0")) {
            return;
        }

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) { //in case the player enters something that isnt an int
            System.out.println("Invalid choice");
            return;
        }

        if (choice < 1 || choice > upgradeables.size()) { //if the player enters a number that is negative or greater than the # of upgradeable units (out of bounds)
            System.out.println("Invalid choice");
            return;
        }

        IUpgradeable selected = upgradeables.get(choice - 1); //get the correct unit (which is at the choice - 1 because of how list indexing works (Ex. choice 1 will be at index 0)
        int currentLevel = selected.getStats().level();
        List<EntityStats> levels = EntityLevelData.getLevels(selected.getEntityType());
        int maxLevel = (levels != null) ? levels.size() : currentLevel;

        if (currentLevel >= maxLevel) {
            System.out.println(selected.getEntityType() + " is already at max level.");
            return;
        }

        try {
            engine.upgrade(player, selected);
            System.out.println(selected.getEntityType() + " upgrade scheduled successfully!");
        } catch (GameEngine.NotEnoughResourcesException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (GameEngine.UpgradeFailedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (GameEngine.QueueFullException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void displayQueues(Village village, GameEngine engine) {
        long currentTime = engine.getGameTime().getTime();

        //build/upgrade Queue
        System.out.println("\n=== Build / Upgrade Queue ===");
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
        System.out.println("\n=== Training Queue ===");
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
}
