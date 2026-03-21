import Game.*;
import UtilThings.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        Player player = new Player();
        File xmlFile = new File("playerVillage.xml");

        //check if there is an existing save from previous games
        if (xmlFile.exists() && xmlFile.length() > 0) {
            try {
                player.setVillage(VillageLoader.XMLtoVillage("playerVillage.xml", player));
                GameLogger.log("Existing village loaded successfully.");
            } catch (Exception e) {
                GameLogger.log("Failed to load save: " + e.getMessage() + ". Creating new village instead.");
                createNewVillage(player);
            }
        }
        else {
            createNewVillage(player);
        }

        engine.addVillage(player.getVillage());

        //this is used for the real time, really no other way to run the real time systems of the game
        Thread gameThread = new Thread(() -> engine.run());
        gameThread.setDaemon(true);
        gameThread.start();

        Controller controller = new Controller(engine, player);
        controller.startGame();

        try {
            VillageSaver.villageToXML(player.getVillage(), "playerVillage.xml", engine.getGameTime().getTime());
        } catch (ParserConfigurationException e) {
            System.err.println("Village Failed to Save: " + e.getMessage());
        } catch (TransformerException e) {
            System.err.println("Transformer Error: " + e.getMessage());
        }
    }

    private static void createNewVillage(Player player) {
        player.setVillage(VillageBuilderDirector.buildNewPlayerVillage(player));
        GameLogger.log("New village created.");
    }


}
