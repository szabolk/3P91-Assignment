import Game.GameEngine;
import Game.Village;
import GameComponents.*;
import Game.Army;
import UtilThings.EntityStats;

public class Main {
    public static void main(String[] args) {
        System.out.println("Compiled Properly");
        GameEngine gameEngine = new GameEngine();
        Village village = new Village();
        //test village hall level
        System.out.println("Village Hall Level: " + village.getVillageHall().getStats().level());
        System.out.println("Current Game Time: " + gameEngine.getGameTime().getTime());
        //Testing worker functionality
        ResourceWorker basicWorker = new ResourceWorker();

        Worker eliteWorker = new Worker(3);

        System.out.println("--- Game Entity Test ---");

        System.out.println("ResourceWorker 1 Level: " + basicWorker.getStats().level());
        System.out.println("ResourceWorker 1 HP: " + basicWorker.getStats().hp());
        System.out.println("ResourceWorker 1 Gold Cost: " + basicWorker.getStats().goldCost());

        System.out.println("------------------------");

        System.out.println("Worker 3 Level: " + eliteWorker.getStats().level());
        System.out.println("Worker 3 HP: " + eliteWorker.getStats().hp());

        if (eliteWorker.getStats().hp() > basicWorker.getStats().hp()) {
            System.out.println("\nSuccess: Elite worker is tankier than basic worker.");
        }

        //Testing ArmyUnit functionality
        System.out.println("\n--- Soldier Combat Test ---");

        Soldier mySoldier = new Soldier(1);

        int maxHp = mySoldier.getStats().hp();
        System.out.println("Soldier Level: " + mySoldier.getStats().level());
        System.out.println("Soldier Starting HP: " + mySoldier.getHP() + "/" + maxHp);

        int damageTaken = 50;
        System.out.println("Soldier takes " + damageTaken + " damage from an arrow!");
        mySoldier.takeDamage(damageTaken);

        System.out.println("Soldier Current HP: " + mySoldier.getHP());

        if (mySoldier.getHP() < maxHp) {
            System.out.println("Success: Soldier is wounded.");
        }
        System.out.println("Soldier ID " + mySoldier.getId());

        //Testing Army getAttackScore
        System.out.println("\n--- Army Attack Score Test ---");

        Army myArmy = new Army();
        System.out.println("Empty Army Attack Score: " + myArmy.getAttackScore());

        Soldier soldier1 = new Soldier(1);
        Soldier soldier2 = new Soldier(2);
        Archer archer1 = new Archer(1);

        myArmy.addUnit(soldier1);
        myArmy.addUnit(soldier2);
        myArmy.addUnit(archer1);

        System.out.println("Army with 2 Soldiers and 1 Archer Attack Score: " + myArmy.getAttackScore());
        System.out.println("Army Unit Count: " + myArmy.getUnits().size());

        myArmy.removeUnit(archer1);
        System.out.println("Army after removing Archer Attack Score: " + myArmy.getAttackScore());
        System.out.println("Current Game Time: " + gameEngine.getGameTime().getTime());
    }
}
