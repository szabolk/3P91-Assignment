package GameComponents;

import UtilThings.EntityType;

public class IronMiner extends ResourceWorker {
    public IronMiner() { this(1); }
    public IronMiner(int level) { super(level); }

    @Override
    public EntityType getEntityType() {
        return EntityType.IRON_MINER;
    }
}
