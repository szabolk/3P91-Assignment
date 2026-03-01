package GameComponents;

import UtilThings.EntityType;

public class LumberMiner extends ResourceWorker {
    public LumberMiner() { this(1); }

    public LumberMiner(int level) { super(level); }

    @Override
    public EntityType getEntityType() {
        return EntityType.LUMBER_MINER;
    }
}
