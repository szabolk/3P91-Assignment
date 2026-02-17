package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

/**
 * This whole class is super important as it provides an id for every entity in the game (needed for
 * upgrading/building/training)
 */
public abstract class Entity implements IUpgradeable {
    private static int nextId = 1;
    private final int id;
    protected EntityStats stats;

    public Entity(EntityStats stats) {
        this.id = nextId++;
        this.stats = stats;
    }

    public int getId() {
        return id;
    }

    public abstract EntityType getEntityType();

    @Override
    public EntityStats getStats() {
        return this.stats;
    }

    @Override
    public void setStats(EntityStats newStats) {
        this.stats = newStats;
    }
}
