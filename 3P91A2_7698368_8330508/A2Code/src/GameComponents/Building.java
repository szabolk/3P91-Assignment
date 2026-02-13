package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

//Stuff related to buildings
public abstract class Building extends Entity implements IAttackable {
    protected EntityStats stats;
    protected int currentHp;
    protected boolean underConstruction;

    public Building(EntityStats stats) {
        super(stats);
    }

    @Override
    public void takeDamage(int damage) {

    }
    @Override
    public boolean isDestroyed() {
        return true;
    }

    @Override
    public int getHP() {
        return 0;
    }
}
