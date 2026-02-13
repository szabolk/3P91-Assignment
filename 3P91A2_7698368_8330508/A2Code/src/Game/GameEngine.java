package Game;

import java.util.List;

//Controls the game (maybe do a canUpdate/canTrain/canBuild here as well?)
public class GameEngine {
    private Time gameTime;
    private List<Village> villages;


    public GameEngine() {

    }

    /**
     * Should be the game loop
     */
    public void runGame() {

    }

    /**
     * When the player chooses to explore an attack, it uses this method to generate a suitable
     * village for the player to possibly attack
     *
     * @param playerVillage - the player's village
     * @return Village - returns a suitable village
     */
    public Village generateVillage(Village playerVillage) {
        return null;
    }

    /**
     * Essentially what it does is it takes an attacking army, a villages defences,
     * calculates the attack score (of the army) and the defence score (of the defences)
     * and then calculates the odds and determines if the attackers win the battle
     *
     * @param attacker the attacker's army
     * @param defender the defender's defences
     * @return SimulationResult which will be used in a separate method to handle the results
     * (like adding the loot to the attacker's resources, increasing num of wins etc.
     */
    public SimulationResult simulateAttack(Army attacker, Defences defender) {
        return null;
    }


    /**
     * After a village (player only?) gets attacked, after the attack the village goes into
     * guard mode for a set period of time (offset from the current time)
     *
     * @param village - village that was attacked
     */
    public void setGuardTime(Village village) {

    }

}
