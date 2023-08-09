package battleship;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class not only have its placement, its DisplayInfo, its name, but also contains a boolean
 * to decide whether this ship contains the upper left block of the passed-in Placement
 * @param <T>
 */
public class DesignedCarrierShip<T> extends BasicShip<T> implements Serializable {

    Placement place;

    ShipDisplayInfo<T> myDisplayInfo;
    final String name; //the name for the ship


    int isTopLeftNull; //true if this shape of ship contains the topLeft block




//    public DesignedCarrierShip(String name, Placement place,
//                              ShipDisplayInfo<T> tShipDisplayInfoSelf, ShipDisplayInfo<T> tShipDisplayInfoEnemy) {
//        super(makeCarrierCoords(place), tShipDisplayInfoSelf, tShipDisplayInfoEnemy);
//        //Check whether the topLeft is occupied
//        if(myPieces.containsKey(place.getWhere())){
//            this.isTopLeftNull = 1;
//        }
//        else{
//            this.isTopLeftNull = 0;
//        }
//        this.name = name;
//    }
    public DesignedCarrierShip(String name, Placement place,
                              ShipDisplayInfo<T> tShipDisplayInfoSelf, ShipDisplayInfo<T> tShipDisplayInfoEnemy) {
        super(makeCarrierCoords(place), tShipDisplayInfoSelf, tShipDisplayInfoEnemy);
        //Check whether the topLeft is occupied
        if(myPieces.containsKey(place.getWhere())){
            this.isTopLeftNull = 1;
        }
        else{
            this.isTopLeftNull = 0;
        }
        this.name = name;
    }

    /**
     *  In the order of self(data if not hit, onHit(*) if hit) and enemy(null if not hit, data if hit)
     * @param name
     * @param place include UDLR information
     * @param data
     * @param onHit
     */
    public DesignedCarrierShip(String name, Placement place, T data, T onHit) {
        this(name, place, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

//    public DesignedCarrierShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
//        super(where, myDisplayInfo, enemyDisplayInfo);
//        this.name = null;
//    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * This method DIYs carrier ship shapes. Since all basic elements are rectangle, I reused the rectangle create method
     * Creating Battleships
     * @param upperLeft_info it contains the upperLeft info as well as UDLR to generate new ship based on input
//     * @param width, w2 splits all designed ships into small rectangles(two sub-rectangles) and w1, w2 are their widths
//     * @param height, h2 splits all designed ships into small rectangles(two sub-rectangles) and w1, w2 are their heights
     * @return the set of Coordinates that contains all the locations of
     */
//    static HashSet<Coordinate> makeCarrierCoords(Placement upperLeft_info){
//        HashSet<Coordinate> ans = new HashSet<>();
//        Coordinate upperLeft = upperLeft_info.getWhere();
//        int row = upperLeft.getRow();
//        int col = upperLeft.getCol();
//        Character orientation = upperLeft_info.getOrientation();
//        if(orientation == 'U'){
//            ans.add(new Coordinate(row, col));
//            ans.add(new Coordinate(row + 1, col));
//            ans.add(new Coordinate(row + 2, col));
//            ans.add(new Coordinate(row + 3, col));
//
//            ans.add(new Coordinate(row + 2, col + 1));
//            ans.add(new Coordinate(row + 3, col + 1));
//            ans.add(new Coordinate(row + 4, col + 1));
//        }
//        if(orientation == 'R') {
//            ans.add(new Coordinate(row, col + 1));
//            ans.add(new Coordinate(row, col + 2));
//            ans.add(new Coordinate(row, col + 3));
//            ans.add(new Coordinate(row, col + 4));
//
//            ans.add(new Coordinate(row + 1, col));
//            ans.add(new Coordinate(row + 1, col + 1));
//            ans.add(new Coordinate(row + 1, col + 2));
//        }
//        if(orientation == 'D'){
//            ans.add(new Coordinate(row, col));
//            ans.add(new Coordinate(row + 1, col));
//            ans.add(new Coordinate(row + 2, col));
//
//            ans.add(new Coordinate(row + 1, col + 1));
//            ans.add(new Coordinate(row + 2, col + 1));
//            ans.add(new Coordinate(row + 3, col + 1));
//            ans.add(new Coordinate(row + 4, col + 1));
//        }
//        if(orientation == 'L'){
//            ans.add(new Coordinate(row, col + 1));
//            ans.add(new Coordinate(row, col + 2));
//            ans.add(new Coordinate(row, col + 3));
//
//            ans.add(new Coordinate(row + 1, col));
//            ans.add(new Coordinate(row + 1, col + 1));
//            ans.add(new Coordinate(row + 1, col + 2));
//            ans.add(new Coordinate(row + 1, col + 3));
//        }
//        return ans;
//    }

    /**
     * 1 - 7 represents 7 locations
     * @param upperLeft_info
     * @return
     */

    static HashMap<Coordinate, Integer> makeCarrierCoords(Placement upperLeft_info){
        HashMap<Coordinate, Integer> ans = new HashMap<Coordinate, Integer>();
        Coordinate upperLeft = upperLeft_info.getWhere();
        int row = upperLeft.getRow();
        int col = upperLeft.getCol();
        Character orientation = upperLeft_info.getOrientation();
        if(orientation == 'U'){
            ans.put(new Coordinate(row, col), 1);
            ans.put(new Coordinate(row + 1, col), 2);
            ans.put(new Coordinate(row + 2, col), 3);
            ans.put(new Coordinate(row + 3, col), 4);

            ans.put(new Coordinate(row + 2, col + 1), 5);
            ans.put(new Coordinate(row + 3, col + 1), 6);
            ans.put(new Coordinate(row + 4, col + 1), 7);
        }
        if(orientation == 'R') {
            ans.put(new Coordinate(row, col + 4), 1);
            ans.put(new Coordinate(row, col + 3), 2);
            ans.put(new Coordinate(row, col + 2), 3);
            ans.put(new Coordinate(row, col + 1), 4);

            ans.put(new Coordinate(row + 1, col + 2), 5);
            ans.put(new Coordinate(row + 1, col + 1), 6);
            ans.put(new Coordinate(row + 1, col), 7);
        }
        if(orientation == 'D'){
            ans.put(new Coordinate(row + 4, col + 1), 1);
            ans.put(new Coordinate(row + 3, col + 1), 2);
            ans.put(new Coordinate(row + 2, col + 1), 3);
            ans.put(new Coordinate(row + 1, col + 1), 4);

            ans.put(new Coordinate(row + 2, col), 5);
            ans.put(new Coordinate(row + 1, col), 6);
            ans.put(new Coordinate(row, col), 7);
        }
        if(orientation == 'L'){
            ans.put(new Coordinate(row + 1, col), 1);
            ans.put(new Coordinate(row + 1, col + 1), 2);
            ans.put(new Coordinate(row + 1, col + 2), 3);
            ans.put(new Coordinate(row + 1, col + 3), 4);

            ans.put(new Coordinate(row, col + 2), 5);
            ans.put(new Coordinate(row, col + 3), 6);
            ans.put(new Coordinate(row, col + 4), 7);
        }
        return ans;
    }

}
