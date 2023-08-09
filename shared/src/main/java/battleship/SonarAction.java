package battleship;

public class SonarAction extends Action{
    /**
     * This constructor only takes the source, which is the destination in the enemy to attack
     * @param _source
     */
    public SonarAction(Coordinate _source) {
        super("S", _source, new Placement("A0V"), 3);
    }
}
