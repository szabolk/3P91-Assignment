package UtilThings;

import GameComponents.*;

public class EntityCreator {
    public static Inhabitant createNewInhabitant(EntityType type) {
        return switch (type) {
            case SOLDIER -> new Soldier();
            case ARCHER -> new Archer();
            case KNIGHT -> new Knight();
            case CATAPULT -> new Catapult();
            case RESOURCE_WORKER -> new ResourceWorker();
            case WORKER -> new Worker();
            default -> throw new IllegalArgumentException("Not an inhabitant: " + type);
        };
    }

    public static Building createNewBuilding(EntityType type) {
        return switch (type) {
            case GOLD_MINE -> new GoldMine();
            case IRON_MINE -> new IronMine();
            case LUMBER_MILL -> new LumberMill();
            case FARM -> new Farm();
            case VILLAGE_HALL -> new VillageHall();
            case ARCHER_TOWER -> new ArcherTower();
            case CANNON -> new Cannon();
            default -> throw new IllegalArgumentException("Not a building: " + type);
        };
    }
}
