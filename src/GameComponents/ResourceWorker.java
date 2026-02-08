package GameComponents;

import UtilThings.*;

//Will not compile for now (need to create the stats for the constructor) --> See Worker.java for how it will look
public class ResourceWorker extends Inhabitant {
    private int productionRate;

    ResourceWorker() {
        super(new EntityStats(EntityLevelData.WORKER_LEVELS.get(0)));
        //this.productionRate = then grab the stat from the entity stat list
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.RESOURCE_WORKER;
    }

}
