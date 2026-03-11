package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Soldier extends ArmyUnit {
    public Soldier() {
        this(1);
    }

    public Soldier(int level) {
        super(EntityLevelData.SOLDIER_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SOLDIER;
    }
}
