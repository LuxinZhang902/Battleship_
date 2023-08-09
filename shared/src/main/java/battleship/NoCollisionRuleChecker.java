package battleship;

public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {

    public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * This method checks whether the ship would be overlapped with some other ships
     * It is to check the board at the position that the ship will take is empty
     * @param theShip
     * @param theBoard
     * @return true if it can be placed, false if it cannot
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        int h = theBoard.getHeight();
        int w = theBoard.getWidth();
        Iterable<Coordinate> coordinates = theShip.getCoordinates();
//        theBoard.tryAddShip(theShip);
        for(Coordinate coor: coordinates) {
            if(theBoard.whatIsAtForSelf(coor) != null){
                return "That placement is invalid: the ship overlaps another ship.";
            }
        }
        return null;
    }

}
