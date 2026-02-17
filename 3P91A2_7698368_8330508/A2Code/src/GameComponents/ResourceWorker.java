package GameComponents;

import UtilThings.*;

public class ResourceWorker extends Inhabitant {
    private int productionRate;

    public ResourceWorker() {
        this(1);
    }

    public ResourceWorker(int level) {
        super(EntityLevelData.RESOURCE_WORKER_LEVELS.get(level - 1));
        this.productionRate = stats.productionRate();
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.RESOURCE_WORKER;
    }

    public int getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }
}
