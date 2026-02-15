package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Catapult extends ArmyUnit {
    public Catapult() {
        this(1);
    }

    public Catapult(int level) {
        super(EntityLevelData.CATAPULT_LEVELS.get(level - 1));
    }
    @Override
    public EntityType getEntityType() {
        return EntityType.CATAPULT;
    }
}
