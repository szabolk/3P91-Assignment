package Game;

//Information about a player
public class Player {
    private static int nextID = 1;
    private int playerID;
    private Village village;
    private int wintotal;
    private int defenseVictory;

    public Player() {
        this.playerID = nextID++;
        this.village = new Village(this);
        this.wintotal = 0;
        this.defenseVictory = 0;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public Village getVillage() {
        return this.village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public int getWintotal() {
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
}
