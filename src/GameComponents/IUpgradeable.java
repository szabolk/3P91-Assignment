package GameComponents;

import UtilThings.EntityStats;

public interface IUpgradeable {
    public String getEntityType();
    public EntityStats getStats();
    public void setStats(EntityStats newStats);


    default void upgrade(EntityStats nextLevelStats) {
        //Upgrading should be the same across all entities
    }
}
