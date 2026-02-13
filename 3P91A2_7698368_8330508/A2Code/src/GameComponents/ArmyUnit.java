package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

//Stuff related to army units
public abstract class ArmyUnit extends Inhabitant implements IAttacker, IAttackable {
    protected EntityStats stats;
    protected int hp;
    protected int damage;
    protected int range;

    public ArmyUnit(EntityStats stats) {
        super(stats);
        this.hp = stats.hp();
        this.damage = stats.damage();
        this.range = stats.range();
    }

    @Override
    public int attack(IAttackable target) {
        return 0;
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
        return 0;
    }


    public int getRange() {
        return 0;
    }


    public int getHP() {
        return this.hp;
    }
}
