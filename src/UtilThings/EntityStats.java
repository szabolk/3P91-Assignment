package UtilThings;

public class EntityStats {
    private final int level;
    private final int hp;
    private final int damage;
    private final int range;
    private final int productionRate;
    private final int goldCost;
    private final int ironCost;
    private final int lumberCost;
    private final int timeToCompletion;
    private final int villageHallReq;

    //For now, any stat not relevant to an entity, the value will just be 0. Ex: A soldier will not have a productionRate
    public EntityStats(int level, int hp, int damage, int range, int productionRate,
                       int goldCost, int ironCost, int lumberCost, int timeToCompletion, int villageHallReq) {
        this.level = level;
        this.hp = hp;
        this.damage = damage;
        this.range = range;
        this.productionRate = productionRate;
        this.goldCost = goldCost;
        this.ironCost = ironCost;
        this.lumberCost = lumberCost;
        this.timeToCompletion = timeToCompletion;
        this.villageHallReq = villageHallReq;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getProductionRate() {
        return productionRate;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public int getIronCost() {
        return ironCost;
    }

    public int getLumberCost() {
        return lumberCost;
    }

    public int getTimeToCompletion() {
        return timeToCompletion;
    }

    public int getVillageHallReq() {
        return villageHallReq;
    }

    public void printStats(EntityStats entityStatistics) {

    }
}
