package GameComponents;

import UtilThings.EntityStats;

public abstract class DefenceBuilding extends Building implements IAttacker {
    protected int damage;
    protected int range;

    public DefenceBuilding(EntityStats stats) {
        super(stats);

    }

    @Override
    public int attack(IAttackable target) {
        return 0;
    }
}
