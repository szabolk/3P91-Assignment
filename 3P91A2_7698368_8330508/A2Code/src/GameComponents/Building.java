package GameComponents;

import UtilThings.EntityStats;

//Stuff related to buildings
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

    @Override
    public void setStats(EntityStats newStats) {
        super.setStats(newStats);
        this.maxHP = newStats.hp();
        this.currentHp = newStats.hp();
    }

    public boolean isUnderConstruction() {
        return this.underConstruction;
    }

    public void setUnderConstruction(boolean status) {
        this.underConstruction = status;
    }
}
