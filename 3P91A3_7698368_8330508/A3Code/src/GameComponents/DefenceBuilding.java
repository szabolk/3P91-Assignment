package GameComponents;

import UtilThings.EntityStats;

/**
 * Used by the defence buildings (archer tower and cannon)
 */
public abstract class DefenceBuilding extends Building implements IAttacker {
    protected int damage;
    protected int range;

    public DefenceBuilding(EntityStats stats) {
        super(stats);
        this.damage = stats.damage();
        this.range = stats.range();
    }

    public int getDamage() {
        return this.damage;
    }

    public int getRange() {
        return this.range;
    }
}
