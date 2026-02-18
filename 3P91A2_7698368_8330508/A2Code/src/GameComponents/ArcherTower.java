package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class ArcherTower extends ArmyUnit{
    public ArcherTower() {
        this(1);
    }

    public ArcherTower(int level) {
        super(EntityLevelData.ARCHER_TOWER_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ARCHER_TOWER;
    }
}
