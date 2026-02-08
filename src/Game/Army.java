package Game;

import GameComponents.ArmyUnit;
import java.util.List;

//Army for each village
public class Army {
    private List<ArmyUnit> units;
    private int attackScore;

    public Army() {

    }

    public void addUnit(ArmyUnit unit) {

    }

    public void removeUnit(ArmyUnit unit){

    }

    public List<ArmyUnit> getUnits() {
        return null;
    }

    /**
     * Gets the attack score
     * @return int - attack score used by GameEngine in attack simulation
     */
    public int getAttackScore() {
        return 0;
    }
}
