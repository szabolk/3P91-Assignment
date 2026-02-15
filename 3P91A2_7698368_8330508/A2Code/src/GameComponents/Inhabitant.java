package GameComponents;

import UtilThings.EntityStats;

//This might not be necessary since it doesnt add anything.
//Update: yeah this is useless. Remove it and change all the classes that extend
//it so they dont anymore - L.S.
public abstract class Inhabitant extends Entity {
    Inhabitant (EntityStats stats) {
        super(stats);
    }
}
