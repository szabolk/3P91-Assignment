package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

/**
 * This class defines all behaviours shared between the inhabitants and buildings
 */
public abstract class Entity implements IUpgradeable {
    protected EntityStats stats;

    public Entity(EntityStats stats) {
        this.stats = stats;
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
