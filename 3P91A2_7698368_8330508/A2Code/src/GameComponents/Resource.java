package GameComponents;

import java.util.List;

public class Resource {
    private int gold;
    private int iron;
    private int lumber;

    private int maxGold;
    private int maxIron;
    private int maxLumber;

    private List<GoldMine> goldMines;
    private List<IronMine> ironMines;
    private List<LumberMill> lumberMills;

    private int goldProductionRate;
    private int ironProductionRate;
    private int woodProductionRate;



    public Resource(int gold, int iron, int lumber) {

    }

    public int getGold() {
        return 0;
    }
    public void addGold(int amount) {

    }

    public int getIron() {
        return 0;
    }
    public void addIron(int amount) {

    }

    public int getLumber() {
        return 0;
    }

    public void addLumber(int amount) {

    }

    //could maybe even have just (Resource cost) as the parameter
    public void spend(int gold, int iron, int lumber) {

    }

    public boolean hasEnough(int gold, int iron, int lumber) {
        return true;
    }


}
