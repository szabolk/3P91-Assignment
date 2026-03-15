package GameComponents;

import UtilThings.ResourceType;
import Game.Village;

/**
 * This class defines the structure of how resources are handled and managed
 */
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

    public void setResources(int gold, int iron, int lumber) {
        this.gold = gold;
        this.iron = iron;
        this.lumber = lumber;
    }

    /**
     * Determines the max amount of any resource a village can hold at any time
     * @param type - used to find the max amount of that type of resource
     * @return - returns the max resource value a village can have at a single time
     */
    public int getMaxResource(ResourceType type) {
        int villageHallLevel = village.getVillageHall().getStats().level();
        int max = 1000 * villageHallLevel;

        //in case each resource should have a different max
        return switch (type) {
            case GOLD -> max;
            case IRON -> max;
            case LUMBER -> max;
        };
    }

    /**
     * Adds a particular resource to the village total. If the amount of resources added makes it
     * so the amount of that resource is greater than the max, it will just maintain the resource as
     * its predefined max value
     * @param type - resource to add to
     * @param amount - the amount of resource to be added
     */
    public void addResource(ResourceType type, int amount) {
        int max = getMaxResource(type);
        switch (type) {
            case GOLD -> gold = Math.min(gold + amount, max);
            case IRON -> iron = Math.min(iron + amount, max);
            case LUMBER -> lumber = Math.min(lumber + amount, max);
        }
    }

    public void spend(int gold, int iron, int lumber) {
        this.gold -= gold;
        this.iron -= iron;
        this.lumber -= lumber;
    }

    public boolean hasEnough(int gold, int iron, int lumber) {
        return (this.gold >= gold & this.iron >= iron & this.lumber >= lumber);
    }
}


