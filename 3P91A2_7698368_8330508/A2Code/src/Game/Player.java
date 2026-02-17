package Game;

//Information about a player
public class Player {
    private static int nextID = 1;
    private int playerID;
    private Village village;

    public Player(Village village) {
        this.playerID = nextID++;
        this.village = village;
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

    public Village exploreAttack() {
        //generate a village first

        return null;
    }

    public void attackVillage(Village village) {
        //Attack the village bounded to the exploredVillage in gameEngine or somewhere?
        //
    }

}
