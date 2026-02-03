package GameComponents;

import UtilThings.*;

public class ResourceWorker extends Inhabitant {
    private int productionRate;

    ResourceWorker() {
        //See worker class for an idea of how stats are going to work
        super(new EntityStats());
        //this.productionRate = then grab the stat from the entity stat list
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.RESOURCE_WORKER;
    }

}
