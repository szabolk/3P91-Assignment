package Game;

import GameComponents.Resource;

//This is instantiated based on when a village is attacked to determine amount of loot and what not
public class SimulationResult {
    private boolean attackerWin;
    private Resource loot;

    public SimulationResult() {

    }

    public boolean isAttackerWin() {
        return attackerWin;
    }

    public Resource getLoot() {
        return loot;
    }
}
