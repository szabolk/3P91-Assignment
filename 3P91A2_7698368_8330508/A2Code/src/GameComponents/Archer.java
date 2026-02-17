package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Archer extends ArmyUnit{
    public Archer() {
        this(1);
    }

    public Archer(int level) {
        super(EntityLevelData.ARCHER_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ARCHER;
    }
}
