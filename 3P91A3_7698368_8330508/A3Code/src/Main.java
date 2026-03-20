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

        Controller controller = new Controller(engine, player);
        controller.startGame();

        try {
            VillageSaver.villageToXML(player.getVillage(), "playerVillage.xml");
        } catch (ParserConfigurationException e) {
            System.err.println("Village Failed to Save: " + e.getMessage());
        } catch (TransformerException e) {
            System.err.println("Transformer Error: " + e.getMessage());
        }
    }


}
