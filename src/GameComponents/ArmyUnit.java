package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

public abstract class ArmyUnit extends Inhabitant implements IAttacker, IAttackable {
    protected int hp;
    protected int damage;
    protected int range;

    public ArmyUnit() {

    }

    @Override
    public int attack(IAttackable target) {
        return 0;
    }

    @Override
    public void takeDamage(int damage) {

    }

    @Override
    public boolean isDestroyed() {
        return true;
    }

    public int getDamage() {
        return 0;
    }


    public int getRange() {
        return 0;
    }


    public int getHP() {
        return 0;
    }
}
