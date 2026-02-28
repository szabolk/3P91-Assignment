package GameComponents;

import UtilThings.EntityType;

public class GoldMiner extends ResourceWorker {
    public GoldMiner() { this(1); }
    public GoldMiner(int level) { super(level); }

    @Override
    public EntityType getEntityType() {
        return EntityType.GOLD_MINER;
    }
}
