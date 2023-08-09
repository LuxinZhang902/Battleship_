package battleship;

import org.checkerframework.checker.units.qual.A;

public class FireAction extends Action {
    /**
     * This constructor only takes the source, which is the destination in the enemy to attack
     * @param _source
     */
    public FireAction(Coordinate _source) {
        super("F", _source, new Placement("A0V"), Integer.MAX_VALUE);
    }
}
