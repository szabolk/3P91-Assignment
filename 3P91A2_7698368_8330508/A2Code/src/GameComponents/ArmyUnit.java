package GameComponents;

import UtilThings.EntityStats;

//Stuff related to army units
public abstract class ArmyUnit extends Inhabitant implements IAttacker, IAttackable {
    protected int hp;
    protected int maxHP;
    protected int damage;
    protected int range;

    public ArmyUnit(EntityStats stats) {
        super(stats);
        this.hp = stats.hp();
        this.maxHP = stats.hp();
        this.damage = stats.damage();
        this.range = stats.range();
    }

    @Override
    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    @Override
    public boolean isDestroyed() {
        return this.hp <= 0;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getRange() {
        return this.range;
    }

    public int getHP() {
        return this.hp;
    }

    @Override
    public void setStats(EntityStats newStats) {
        super.setStats(newStats);
        this.maxHP = newStats.hp();
        this.hp = newStats.hp();
        this.damage = newStats.damage();
        this.range = newStats.range();
    }
}
