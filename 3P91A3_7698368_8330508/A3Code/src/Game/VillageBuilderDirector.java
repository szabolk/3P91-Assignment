package Game;

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
}
