/*
package UtilThings;

import GameComponents.*;

public interface AbstractEntityFactory {
    ArmyUnit createSoldier();
    ArmyUnit createArcher();
    ArmyUnit createKnight();
    ArmyUnit createCatapult();
    Inhabitant createWorker();
    Inhabitant createGoldMiner();
    Inhabitant createIronMiner();
    Inhabitant createLumberMiner();
    Building createFarm();
    Building createGoldMine();
    Building createIronMine();
    Building createLumberMill();
    Building createVillageHall();
    DefenceBuilding createArcherTower();
    DefenceBuilding createCannon();

    default Building createBuilding(EntityType type) {
        return switch (type) {
            case FARM -> createFarm();
            case GOLD_MINE -> createGoldMine();
            case IRON_MINE -> createIronMine();
            case LUMBER_MILL -> createLumberMill();
            case VILLAGE_HALL -> createVillageHall();
            case ARCHER_TOWER -> createArcherTower();
            case CANNON -> createCannon();
            default -> throw new IllegalArgumentException("Not a building: " + type);
        };
    }

    default Inhabitant createInhabitant(EntityType type) {
        return switch (type) {
            case SOLDIER -> createSoldier();
            case ARCHER -> createArcher();
            case KNIGHT -> createKnight();
            case CATAPULT -> createCatapult();
            case WORKER -> createWorker();
            case GOLD_MINER -> createGoldMiner();
            case IRON_MINER -> createIronMiner();
            case LUMBER_MINER -> createLumberMiner();
            default -> throw new IllegalArgumentException("Not an inhabitant: " + type);
        };
    }
}
*/