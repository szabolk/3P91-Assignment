package GameComponents;

import UtilThings.EntityLevelData;
import UtilThings.EntityStats;
import UtilThings.EntityType;

//Will not compile for now (need to create the stats for the constructor) --> See Worker.java for how it will look
public class Catapult extends ArmyUnit {
    public Catapult() {
        this(1);
    }

    public Catapult(int level) {
        super(EntityLevelData.WORKER_LEVELS.get(level - 1));
    }
    @Override
    public EntityType getEntityType() {
        return null;
    }
}
