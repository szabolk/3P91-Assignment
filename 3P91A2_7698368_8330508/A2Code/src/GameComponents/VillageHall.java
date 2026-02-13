package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class VillageHall extends Building {
    public VillageHall() {
        this(1);
    }

    public VillageHall(int level) {
        super(EntityLevelData.VILLAGE_HALL_LEVELS.get(level - 1));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGE_HALL;
    }
}
