import GameComponents.Soldier;
import GameComponents.Worker;
import UtilThings.EntityStats;

public class Main {
    public static void main(String[] args) {
        System.out.println("Compiled Properly");

        //Testing worker functionality
        Worker basicWorker = new Worker();

        Worker eliteWorker = new Worker(3);

        System.out.println("--- Game Entity Test ---");

        System.out.println("Worker 1 Level: " + basicWorker.getStats().level());
        System.out.println("Worker 1 HP: " + basicWorker.getStats().hp());
        System.out.println("Worker 1 Gold Cost: " + basicWorker.getStats().goldCost());

        System.out.println("------------------------");

        System.out.println("Worker 3 Level: " + eliteWorker.getStats().level());
        System.out.println("Worker 3 HP: " + eliteWorker.getStats().hp());
        System.out.println("Worker 3 Production: " + eliteWorker.getStats().productionRate());

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
    }
}
