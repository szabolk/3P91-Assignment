package GameComponents;

import UtilThings.EntityStats;
import UtilThings.EntityType;

public interface IUpgradeable {
    public EntityType getEntityType();
    public EntityStats getStats();
    public void setStats(EntityStats newStats);
}
