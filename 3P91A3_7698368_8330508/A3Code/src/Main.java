import Game.*;
import UI.UserInterface;
import UtilThings.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        Player player = new Player();
        player.setVillage(VillageBuilderDirector.buildNewPlayerVillage(player));
        engine.addVillage(player.getVillage());
        GameLogger.log("Game started.");

        //this is used for the real time, really no other way to run the real time systems of the game
        Thread gameThread = new Thread(() -> engine.run());
        gameThread.setDaemon(true);
        gameThread.start();

        //main UI loop for the game. Print out the menu, user inputs one of the menu options, and can continue
        //playing the game until they decide to quit by enter 0
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            UserInterface.printMainMenu();
            String line = scanner.nextLine().trim();
            switch (line) {
                case "1" -> UserInterface.explore(player, engine);
                case "2" -> UserInterface.attack(player, engine);
                case "3" -> UserInterface.displayBuildMenu(player, engine);
                case "4" -> UserInterface.displayUpgradeMenu(player, engine);
                case "5" -> UserInterface.displayVillageStatus(player.getVillage());
                case "6" -> UserInterface.printAllBuildings(player.getVillage());
                case "7" -> UserInterface.printAllInhabitants(player.getVillage());
                case "8" -> UserInterface.displayQueues(player.getVillage(), engine);
                case "0" -> {
                    isRunning = false;
                    GameLogger.log("Game ended.\n");
                }
                default -> System.out.println("Invalid Choice");
            }
        }
        try {
            VillageSaver.villageToXML(player.getVillage(), "playerVillage.xml");
        } catch (ParserConfigurationException e) {
            System.err.println("Village Failed to Save: " + e.getMessage());
        } catch (TransformerException e) {
            System.err.println("Transformer Error: " + e.getMessage());
        }
    }


}
