package GameComponents;

import UtilThings.EntityStats;

//Stuff related to buildings
public abstract class Building extends Entity implements IAttackable {
    protected int currentHp;
    protected boolean underConstruction;

    public Building(EntityStats stats) {
        super(stats);
        this.currentHp = stats.hp();
        this.underConstruction = true;
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

    public boolean isUnderConstruction() {
        return this.underConstruction;
    }

    public void setUnderConstruction(boolean status) {
        this.underConstruction = status;
    }
}
