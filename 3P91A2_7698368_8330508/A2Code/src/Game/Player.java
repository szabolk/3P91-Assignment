package Game;

//Information about a player
public class Player {
    private static int nextID = 1;
    private int playerID;
    private Village village;

    public Player() {
        this.playerID = nextID++;
        this.village = new Village(this);
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
}
