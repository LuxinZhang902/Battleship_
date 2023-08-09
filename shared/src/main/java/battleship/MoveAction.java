package battleship;

public class MoveAction extends Action{
    /**
     * This constructor takes the source that the ship is moved from, and the placement that the ship is moved to.
     * @param _source
     * @param _moveTo
     */
    public MoveAction(Coordinate _source, Placement _moveTo) {
        super("M", _source, _moveTo, 3);
    }
}
