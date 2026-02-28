import Game.*;
import GameComponents.*;
import Game.Army;
import UtilThings.EntityStats;
import UtilThings.EntityLevelData;
import UtilThings.EntityType;
import UtilThings.ResourceType;
import java.util.Scanner;

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
                case "1" -> System.out.println("1 Works");
                case "2" -> displayBuildMenu(player, engine);
                case "3" -> System.out.println("3 Works");
                case "4" -> displayVillageStatus(player.getVillage());
                case "0" -> isRunning = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }


    //maybe add to see what things are in the build / train queue, think of other qol things maybe? WOOOO ITS 11pm
    private static void printMainMenu() {
        System.out.println("\n=== Player Menu ===");
        System.out.println("1. Explore for attack");
        System.out.println("2. Build / Train");
        System.out.println("3. Upgrade Building / Unit");
        System.out.println("4. View village status");
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

        System.out.println("\nInhabitants: " + village.getInhabitants().size());

        System.out.println("Buildings: " + village.getBuildings().size());

        System.out.println("Army Units: " + village.getArmy().getUnits().size());

        System.out.println("Defence Buildings: " + village.getDefences().getDefenceBuildings().size());
        
        System.out.println("\nPlayer Stats:");
        System.out.println("  Attack Wins: " + village.getOwner().getWintotal());
        System.out.println("  Defense Wins: " + village.getOwner().getDefenseVictory());
        System.out.println();
    }

    private static void displayBuildMenu(Player player, GameEngine engine) {
        Village village = player.getVillage();
        Scanner buildScanner = new Scanner(System.in);
        boolean buildMenuOpen = true;

        while (buildMenuOpen) {
            System.out.println("\n=== Build / Train Menu ===");
            System.out.println("Buildings:");
            System.out.println("1. Farm (50g, 0i, 80l) - Produces food");
            System.out.println("2. Gold Mine (0g, 50i, 100l) - Produces gold");
            System.out.println("3. Iron Mine (100g, 0i, 100l) - Produces iron");
            System.out.println("4. Lumber Mill (100g, 0i, 0l) - Produces lumber");
            System.out.println("5. Archer Tower (100g, 50i, 150l) - Defence building");
            System.out.println("6. Cannon (200g, 150i, 100l) - Defence building");
            System.out.println("\nUnits:");
            System.out.println("7. Soldier (60g, 20i, 0l)");
            System.out.println("8. Archer (50g, 0i, 40l)");
            System.out.println("9. Knight (150g, 100i, 0l)");
            System.out.println("10. Catapult (200g, 150i, 200l)");
            System.out.println("0. Return to main menu");
            System.out.print("Choice: ");

            String buildChoice = buildScanner.nextLine().trim();
            
            Entity entity = null;
            switch (buildChoice) {
                case "1" -> entity = new Farm();
                case "2" -> entity = new GoldMine();
                case "3" -> entity = new IronMine();
                case "4" -> entity = new LumberMill();
                case "5" -> entity = new ArcherTower();
                case "6" -> entity = new Cannon();
                case "7" -> entity = new Soldier();
                case "8" -> entity = new Archer();
                case "9" -> entity = new Knight();
                case "10" -> entity = new Catapult();
                case "0" -> buildMenuOpen = false;
                default -> System.out.println("Invalid choice");
            }

            if (entity != null) {
                try {
                    engine.buildOrTrain(player, entity);
                    System.out.println(entity.getEntityType() + " scheduled successfully!");
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
}