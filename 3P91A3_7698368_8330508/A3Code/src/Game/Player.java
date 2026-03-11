package Game;

/**
 * Used to hold information about a player, like their specific village, id, wins, losses, etc.
 */
public class Player {
    private static int nextID = 1;
    private int playerID;
    private Village village;
    private int wintotal;
    private int defenseVictory;
    private int lossTotal;
    private int defenseLosses;
    private Village exploredVillage;

    public Player() {
        this.playerID = nextID++;
        this.village = null;
        this.wintotal = 0;
        this.defenseVictory = 0;
        this.lossTotal = 0;
        this.defenseLosses = 0;
        this.exploredVillage = null;
    }

    //All these methods are just getters/setter or super basic, no need to comment them all

    public int getPlayerID() {
        return this.playerID;
    }

    public Village getVillage() {
        return this.village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public int getWinTotal() {
        return this.wintotal;
    }

    public void addWin() {
        this.wintotal++;
    }

    public int getDefenseVictory() {
        return this.defenseVictory;
    }

    public void addDefenseVictory() {
        this.defenseVictory++;
    }

    public int getLossTotal() {
        return this.lossTotal;
    }

    public void addLoss() {
        this.lossTotal++;
    }

    public int getDefenseLosses() {
        return this.defenseLosses;
    }

    public void addDefenseLoss() {
        this.defenseLosses++;
    }

    public Village getExploredVillage() {
        return exploredVillage;
    }

    public void setExploredVillage(Village exploredVillage) {
        this.exploredVillage = exploredVillage;
    }
}
