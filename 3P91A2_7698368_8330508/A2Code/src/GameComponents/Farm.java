package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

import java.util.List;

public class Farm extends Building {
    private List<Worker> workers;

    public Farm() {
        this(1);
    }

    public Farm(int level) {
        super(EntityLevelData.FARM_LEVELS.get(level - 1));
    }
    @Override
    public EntityType getEntityType() {
        return EntityType.FARM;
    }
}
