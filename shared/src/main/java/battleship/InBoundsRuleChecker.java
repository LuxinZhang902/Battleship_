package battleship;


public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

    public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        int h = theBoard.getHeight();
        int w = theBoard.getWidth();
        Iterable<Coordinate> coordinates = theShip.getCoordinates();
        for(Coordinate coor: coordinates){
            int r = coor.getRow();
            int c = coor.getCol();
            //if there exists row or col out of bound, return false
            //The bound can actually not be less than 0 since coordinate throws this problem already
//            if((r < 0 || r >= h) || (c < 0 || c >= w)){
//                return false;
//            }
//            if(r < 0){
//                return "That placement is invalid: the ship goes off the top of the board.";
//            }
            if(r >= h){
                return "That placement is invalid: the ship goes off the bottom of the board.";
            }
//            if(c < 0){
//                return "That placement is invalid: the ship goes off the left of the board.";
//            }
            if(c >= w){
                return "That placement is invalid: the ship goes off the right of the board.";
            }
        }

        return null;
    }
}
