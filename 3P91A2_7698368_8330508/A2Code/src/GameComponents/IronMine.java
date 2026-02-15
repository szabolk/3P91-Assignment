package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class IronMine extends ResourceBuilding {
    public IronMine() {
        this(1);
    }

    public IronMine(int level) {
        super(EntityLevelData.IRON_MINE_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.IRON_MINE;
    }
}
