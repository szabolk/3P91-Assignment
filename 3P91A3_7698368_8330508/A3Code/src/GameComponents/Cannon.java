package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Cannon extends DefenceBuilding{
    public Cannon() {
        this(1);
    }

    public Cannon(int level) {
        super(EntityLevelData.CANNON_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.CANNON;
    }
}
