package Game;

import GameComponents.Resource;

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
