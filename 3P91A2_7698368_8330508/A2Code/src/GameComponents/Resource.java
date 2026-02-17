package GameComponents;

import UtilThings.ResourceType;
import Game.Village;

public class Resource {
    private int gold;
    private int iron;
    private int lumber;
    private Village village;

    public Resource(Village village, int gold, int iron, int lumber) {
        this.village = village;
        this.gold = gold;
        this.iron = iron;
        this.lumber = lumber;
    }

    public int getGold() {
        return this.gold;
    }

    public int getIron() {
        return this.iron;
    }

    public int getLumber() {
        return this.lumber;
    }

    public int getMaxResource(ResourceType type) {
        int villageHallLevel = village.getVillageHall().getStats().level();
        int max = 750 * villageHallLevel;
        
        return switch (type) {
            case GOLD -> max;
            case IRON -> max;
            case LUMBER -> max;
        };
    }

    public void addResource(ResourceType type, int amount) {
        int max = getMaxResource(type);
        switch (type) {
            case GOLD -> gold = Math.min(gold + amount, max);
            case IRON -> iron = Math.min(iron + amount, max);
            case LUMBER -> lumber = Math.min(lumber + amount, max);
        }
    }

    //could maybe even have just (Resource cost) as the parameter
    public void spend(int gold, int iron, int lumber) {

    }

    public boolean hasEnough(int gold, int iron, int lumber) {
        return true;
    }
}


