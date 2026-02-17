package Game;

import GameComponents.DefenceBuilding;
import GameComponents.IAttacker;

import java.util.ArrayList;
import java.util.List;

//Defence buildings for each village
public class Defences {
    private List<DefenceBuilding> defenceBuildings;

    public Defences() {
        this.defenceBuildings = new ArrayList<>();
    }


    public void addDefenceBuilding(DefenceBuilding building) {
        if (building != null) {
            defenceBuildings.add(building);
        }
    }

    public void removeDefenceBuilding(DefenceBuilding building) {
        if (building != null) {
            defenceBuildings.remove(building);
        }
    }

    public List<DefenceBuilding> getDefenceBuildings() {
        return defenceBuildings;
    }

    /**
     * Gets the defence score
     * @return int - gives back a value used by the GameEngine in the attack simulations
     */
    public int getDefenceScore() {
        return defenceBuildings.stream()
                .filter(building -> building != null)
                .mapToInt(building -> ((IAttacker) building).getDamage())
                .sum();
    }
}
