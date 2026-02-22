package Game;

import GameComponents.Resource;

//This is instantiated based on when a village is attacked to determine amount of loot and what not
public class SimulationResult {
    private boolean attackerWin;
    private Resource loot;
    private double successPercentage;

    public SimulationResult(boolean attackerWin, Resource loot, double successPercentage) {
        this.attackerWin = attackerWin;
        this.loot = loot;
        this.successPercentage = successPercentage;
    }

    public boolean isAttackerWin() {
        return attackerWin;
    }

    public Resource getLoot() {
        return loot;
    }

    public double getSuccessPercentage() {
        return successPercentage;
    }

    public void setAttackerWin(boolean attackerWin) {
        this.attackerWin = attackerWin;
    }

    public void setLoot(Resource loot) {
        this.loot = loot;
    }

    public void setSuccessPercentage(double successPercentage) {
        this.successPercentage = successPercentage;
    }
}
