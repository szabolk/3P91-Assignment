package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityType;

public class Farm extends Building {
    private static final int POPULATION_PER_FARM = 5;

    public Farm() {
        this(1);
    }

    public Farm(int level) {
        super(EntityLevelData.FARM_LEVELS.get(level - 1));
    }

    public int supportedPopulation() {
        return POPULATION_PER_FARM;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.FARM;
    }
}
