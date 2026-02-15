package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class GoldMine extends ResourceBuilding {
    public GoldMine() {
        this(1);
    }

    public GoldMine(int level) {
        super(EntityLevelData.GOLD_MINE_LEVELS.get(level - 1));
    }
    @Override
    public EntityType getEntityType() {
        return EntityType.GOLD_MINE;
    }
}
