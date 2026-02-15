package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class LumberMill extends ResourceBuilding {
    public LumberMill() {
        this(1);
    }

    public LumberMill(int level) {
        super(EntityLevelData.LUMBER_MILL_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.LUMBER_MILL;
    }
}
