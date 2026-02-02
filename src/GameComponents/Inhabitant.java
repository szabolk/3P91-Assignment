package GameComponents;

import UtilThings.EntityStats;

public abstract class Inhabitant implements IUpgradeable, IMoveable {
    protected EntityStats stats;
    protected int x;
    protected int y;

    public abstract String getEntityType();

    @Override
    public EntityStats getStats() {
        return this.stats;
    }

    @Override
    public void setStats(EntityStats newStats) {
        //grab the stat profile from EntityLevelData using the current level + 1 to get next stats details
        //will have to work out the logic if the unit should have fully restored hp or not
        //this.stats =
    }

    @Override
    public boolean move(int dx, int dy) {
        //idk how big the map is, dunno what bounds are
        return true;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

}
