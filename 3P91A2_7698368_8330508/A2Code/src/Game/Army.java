package Game;

import GameComponents.ArmyUnit;
import GameComponents.IAttacker;
import java.util.ArrayList;
import java.util.List;

//Army for each village
public class Army {
    private List<ArmyUnit> units;

    public Army() {
        this.units = new ArrayList<>();
    }

    public void addUnit(ArmyUnit unit) {
        if (unit != null) {
            units.add(unit);
        }
    }

    public void removeUnit(ArmyUnit unit){
        if (unit != null) {
            units.remove(unit);
        }
    }

    public List<ArmyUnit> getUnits() {
        return units;
    }

    /**
     * Gets the attack score
     * @return int - attack score used by GameEngine in attack simulation
     */
    public int getAttackScore() {
        return units.stream()
                .filter(unit -> unit != null)
                .mapToInt(unit -> ((IAttacker) unit).getDamage())
                .sum();
    }
}
