package GameComponents;

import UtilThings.EntityStats;

/**
 * The parent class of all buildings (like gold mines or archer towers)
 */
public abstract class Building extends Entity implements IAttackable {
    protected int currentHp;
    protected int maxHP;
    protected boolean underConstruction;

    public Building(EntityStats stats) {
        super(stats);
        this.currentHp = stats.hp();
        this.maxHP = stats.hp();
        this.underConstruction = false; //switched to false for now seeing how it works overall
    }

    @Override
    public void takeDamage(int damage) {
        this.currentHp -= damage;
    }
    @Override
    public boolean isDestroyed() {
        return this.currentHp <= 0;
    }

    @Override
    public int getHP() {
        return this.currentHp;
    }

    /**
     * Updates the dynamic stats (hp) in addition to the static stats
     * @param newStats - new stats
     */
    @Override
    public void setStats(EntityStats newStats) {
        super.setStats(newStats);
        this.maxHP = newStats.hp();
        this.currentHp = newStats.hp();
    }

    //These two methods below are used when calculating defence score, with buildings
    //not under construction only being counted

    public boolean isUnderConstruction() {
        return this.underConstruction;
    }

    public void setUnderConstruction(boolean status) {
        this.underConstruction = status;
    }
}
