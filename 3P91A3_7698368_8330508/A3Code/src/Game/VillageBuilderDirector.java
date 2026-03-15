package Game;

import GameComponents.ArmyUnit;
import GameComponents.Building;
import GameComponents.DefenceBuilding;
import GameComponents.Inhabitant;

import java.util.List;

public class VillageBuilderDirector {

    public static Village buildNewPlayerVillage(Player owner) {
        return new Village.VillageBuilder()
                .owner(owner)
                .hallLevel(1)
                .resources(500, 500, 500)
                .guardedUntil(60000)
                .withStarterEntities()
                .build();
    }

    public static Village buildNpcVillage(int hallLevel, int gold, int iron, int lumber) {
        return new Village.VillageBuilder()
                .hallLevel(hallLevel)
                .resources(gold, iron, lumber)
                .guardedUntil(0)
                .build();
    }

    public static Village buildLoadedVillage(List<Building> buildings, List<Inhabitant> inhabitants, List<ArmyUnit> army,
                                             List<DefenceBuilding> defences, List<Village.QueueTask> buildQueue,
                                             List<Village.QueueTask> trainQueue, int gold, int iron, int lumber,
                                             int hallLevel, long guardedUntil,
                                             int attackWins, int attackLosses, int defenceWins, int defenceLosses) {

        Player owner = new Player();
        owner.setAttackWins(attackWins);
        owner.setAttackLosses(attackLosses);
        owner.setDefenseVictory(defenceWins);
        owner.setAttackLosses(defenceLosses);

        Village newVillage = new Village.VillageBuilder()
                .owner(owner)
                .hallLevel(hallLevel)
                .resources(gold, iron, lumber)
                .guardedUntil(guardedUntil)
                .build();

        newVillage.setBuildings(buildings);
        newVillage.setInhabitants(inhabitants);
        newVillage.setBuildQueue(buildQueue);
        newVillage.setTrainQueue(trainQueue);

        return newVillage;
    }
}
