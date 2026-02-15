package GameComponents;

import UtilThings.EntityStats;

//Stuff related to defence buildings
public abstract class DefenceBuilding extends Building implements IAttacker {
    protected int damage;
    protected int range;

    public DefenceBuilding(EntityStats stats) {
        super(stats);

    }

    @Override
    public void attack(IAttackable target) {
        return 0;
    }
}
