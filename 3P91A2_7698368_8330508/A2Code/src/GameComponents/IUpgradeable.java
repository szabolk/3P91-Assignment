package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

/**
 * Behaviour shared by all things that are upgradeable
 */
public interface IUpgradeable {
    public EntityType getEntityType();
    public EntityStats getStats();
    public void setStats(EntityStats newStats);
}
