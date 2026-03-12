package Game;

import ChallengeDecision.*;
import GameComponents.ArmyUnit;
import GameComponents.DefenceBuilding;
import GameComponents.Resource;

import java.util.ArrayList;
import java.util.List;

public class ChallengeAdapter {

    private final Army army;
    private final Defences defences;
    private final Resource attackerResources;
    private final Resource defenderResources;

    public ChallengeAdapter(Army attackers, Resource attackerResources, Defences defenders, Resource defenderResources) {
        this.army = attackers;
        this.defences = defenders;
        this.attackerResources = attackerResources;
        this.defenderResources = defenderResources;
    }

    public ChallengeResult simulateAttack() {
        ChallengeEntitySet<Double,Double> attackerUnits = convertArmyToAttackers();
        ChallengeEntitySet<Double,Double> defenderUnits = convertDefencesToDefenders();

        return Arbitrer.challengeDecide(attackerUnits, defenderUnits);
    }

    public ChallengeEntitySet<Double, Double> convertArmyToAttackers() {
        List<ChallengeAttack<Double, Double>> attackerList = new ArrayList<>();
        for (ArmyUnit unit : army.getUnits()) {
            ChallengeAttack<Double, Double> attacker = new ChallengeAttack<>((double) unit.getDamage(), (double) unit.getHP());
            attackerList.add(attacker);
        }

        ChallengeEntitySet<Double, Double> attackerEntitySet = new ChallengeEntitySet<>();
        attackerEntitySet.setEntityAttackList(attackerList);
        attackerEntitySet.setEntityDefenseList(new ArrayList<>()); //doesnt make sense for the defences of the attacker to be relevant here, so just keep it empty
        attackerEntitySet.setEntityResourceList(convertResources(this.attackerResources)); //makes it so that attackers are able to lose loot if they fail the attack

        return attackerEntitySet;
    }

    public ChallengeEntitySet<Double, Double> convertDefencesToDefenders() {
        List<ChallengeDefense<Double, Double>> defenderList = new ArrayList<>();
        for (DefenceBuilding b : defences.getDefenceBuildings()) {
            ChallengeDefense<Double, Double> defender = new ChallengeDefense<>((double) b.getDamage(), (double) b.getHP());
            defenderList.add(defender);
        }

        ChallengeEntitySet<Double, Double> defenderEntitySet = new ChallengeEntitySet<>();
        defenderEntitySet.setEntityAttackList(new ArrayList<>()); //similar to the attacking counterpart, the defender's army shouldnt take part
        defenderEntitySet.setEntityDefenseList(defenderList);
        defenderEntitySet.setEntityResourceList(convertResources(this.defenderResources)); //makes it so that attackers are able to lose loot if they fail the attack

        return defenderEntitySet;
    }

    public static List<ChallengeResource<Double, Double>> convertResources(Resource resources) {
        List<ChallengeResource<Double, Double>> resourcesList = new ArrayList<>();
        resourcesList.add(new ChallengeResource<>((double) resources.getGold()));
        resourcesList.add(new ChallengeResource<>((double) resources.getIron()));
        resourcesList.add(new ChallengeResource<>((double) resources.getLumber()));

        return resourcesList;
    }
}
