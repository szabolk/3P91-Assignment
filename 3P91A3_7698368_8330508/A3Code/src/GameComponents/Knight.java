package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Knight extends ArmyUnit {
    public Knight() {
        this(1);
    }

    public Knight(int level) {
        super(EntityLevelData.KNIGHT_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.KNIGHT;
    }
}
