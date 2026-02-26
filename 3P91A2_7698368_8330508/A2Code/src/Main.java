import Game.*;
import GameComponents.*;
import Game.Army;
import UtilThings.EntityStats;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //idk if this goes in the UI class or here is fine
        GameEngine engine = new GameEngine();
        Player player = new Player();
        engine.addVillage(player.getVillage());
        //engine.runGame(); how do we get the underlying game logic to run in the background
        //Thread gameThread = new Thread(); something like this but idk how it works
        //just the skeleton for now, needs to be worked on
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            printMainMenu();
            String line = scanner.nextLine().trim();
            switch (line) {
                case "1" -> System.out.println("1 Works");
                case "2" -> System.out.println("2 Works");
                case "0" -> isRunning = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }



    private static void printMainMenu() {
        System.out.println("\n=== Player Menu ===");
        System.out.println("1. Explore for attack");
        System.out.println("2. Build / Train");
        System.out.println("3. Upgrade Building / Unit");
        System.out.println("4. View village status");
        System.out.println("0. Quit");
        System.out.print("Choice: ");
    }
}
