package Game;

import ChallengeDecision.ChallengeResource;
import ChallengeDecision.ChallengeResult;
import UI.UserInterface;
import UtilThings.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Receives the user's inputs and then makes calls to the model (game engine, etc), and calls the view when the player requests it
 */
public class Controller {
    private final GameEngine engine;
    private final Player player;
    private final Scanner scanner;

    public Controller(GameEngine engine, Player player) {
        this.engine = engine;
        this.player = player;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Used as the main starting point of a game session
     */
    public void startGame() {
        GameLogger.log("Game started.");
        boolean isRunning = true;
        while (isRunning) {
            UserInterface.printMainMenu();
            String line = scanner.nextLine().trim();
            switch (line) {
                case "1" -> handleExplore();
                case "2" -> handleAttack();
                case "3" -> handleBuild();
                case "4" -> handleUpgrade();
                case "5" -> UserInterface.displayVillageStatus(player.getVillage());
                case "6" -> UserInterface.printAllBuildings(player.getVillage());
                case "7" -> UserInterface.printAllInhabitants(player.getVillage());
                case "8" -> UserInterface.displayQueues(player.getVillage(), engine);
                case "0" -> {
                    isRunning = false;
                    GameLogger.log("Game ended.\n");
                }
                default  -> UserInterface.printMessage("Invalid Choice");
            }
        }
    }

    /**
     * Explores a village and sets that to the player's explored village, then displays the corresponding view
     */
    private void handleExplore() {
        Village exploredVillage = engine.exploreAttack(player.getVillage());
        player.setExploredVillage(exploredVillage);
        UserInterface.displayExploredVillage(player.getVillage());
    }

    /**
     * Attacks and handles the result returned from the game engine
     */
    private void handleAttack() {
        try {
            ChallengeResult result = engine.executeAttack(player.getVillage(), player.getExploredVillage());

            if (result.getChallengeWon()) {
                player.addWin();
                List<ChallengeResource<Double, Double>> loot = result.getLoot();
                engine.addLootToPlayer(player, loot);

                int[] resourcesGained = new int[3];
                for (int i = 0; i < loot.size() && i < 3; i++) {
                    resourcesGained[i] = loot.get(i).getProperty().intValue();
                }

                UserInterface.displayAttackWin(resourcesGained[0], resourcesGained[1], resourcesGained[2]);
                GameLogger.log("Player Attack Win — loot: "
                        + resourcesGained[0] + "g " + resourcesGained[1] + "i " + resourcesGained[2] + "l");
            } else {
                player.addLoss();
                UserInterface.displayAttackLoss();
                GameLogger.log("Player Attack Loss");
            }

            // Clear explored village so the player cannot attack the same one twice
            player.setExploredVillage(null);

        } catch (GameEngine.NoVillageExploredException e) {
            UserInterface.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Handles building/training when the player wishes to build/train a unit
     */
    private void handleBuild() {
        boolean buildMenuOpen = true;

        while (buildMenuOpen) {
            UserInterface.printBuildMenu();
            String buildChoice = scanner.nextLine().trim();

            if (buildChoice.equals("0")) {
                buildMenuOpen = false;
                continue;
            }

            EntityType selectedType = getBuildChoice(buildChoice);
            if (selectedType == null) {
                UserInterface.printMessage("Invalid choice");
                continue;
            }

            try {
                engine.buildOrTrain(player, selectedType);
                UserInterface.printMessage(selectedType + " scheduled successfully!");
                GameLogger.log("BUILD/TRAIN queued: " + selectedType);
            } catch (GameEngine.NotEnoughResourcesException e) {
                UserInterface.printMessage("Error: " + e.getMessage());
            } catch (GameEngine.MaxBuildingsExceededException e) {
                UserInterface.printMessage("Error: " + e.getMessage());
            } catch (GameEngine.QueueFullException e) {
                UserInterface.printMessage("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles upgrading when the player selects the option
     */
    private void handleUpgrade() {
        Village village = player.getVillage();

        List<GameComponents.IUpgradeable> upgradeables = new ArrayList<>();
        upgradeables.addAll(village.getBuildings());
        upgradeables.addAll(village.getInhabitants());

        UserInterface.displayUpgradeMenu(upgradeables);

        String input = scanner.nextLine().trim();

        if (input.equals("0")) {
            return;
        }

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            UserInterface.printMessage("Invalid choice: Please choose a number between 1 and "
                    + upgradeables.size());
            return;
        }

        if (choice < 1 || choice > upgradeables.size()) {
            UserInterface.printMessage("Invalid choice: Please select a number found in the list");
            return;
        }

        GameComponents.IUpgradeable selected = upgradeables.get(choice - 1);
        int currentLevel = selected.getStats().level();
        List<UtilThings.EntityStats> levels = EntityLevelData.getLevels(selected.getEntityType());
        int maxLevel = (levels != null) ? levels.size() : currentLevel;

        if (currentLevel >= maxLevel) {
            UserInterface.printMessage(selected.getEntityType() + " is already at max level.");
            return;
        }

        try {
            engine.upgrade(player, selected); //schedules the entity the player selected from the upgrade menu
            UserInterface.printMessage(selected.getEntityType() + " upgrade scheduled successfully!");
            GameLogger.log("Upgrade queued: " + selected.getEntityType()
                    + " to level " + (currentLevel + 1));
        } catch (GameEngine.NotEnoughResourcesException e) {
            UserInterface.printMessage("Error: " + e.getMessage());
        } catch (GameEngine.UpgradeFailedException e) {
            UserInterface.printMessage("Error: " + e.getMessage());
        } catch (GameEngine.QueueFullException e) {
            UserInterface.printMessage("Error: " + e.getMessage());
        }
    }

    private EntityType getBuildChoice(String choice) {
        return switch (choice) {
            case "1" -> EntityType.FARM;
            case "2" -> EntityType.GOLD_MINE;
            case "3" -> EntityType.IRON_MINE;
            case "4" -> EntityType.LUMBER_MILL;
            case "5" -> EntityType.ARCHER_TOWER;
            case "6" -> EntityType.CANNON;
            case "7" -> EntityType.SOLDIER;
            case "8" -> EntityType.ARCHER;
            case "9" -> EntityType.KNIGHT;
            case "10" -> EntityType.CATAPULT;
            case "11" -> EntityType.WORKER;
            case "12" -> EntityType.GOLD_MINER;
            case "13" -> EntityType.IRON_MINER;
            case "14" -> EntityType.LUMBER_MINER;
            default -> null;
        };
    }
}
