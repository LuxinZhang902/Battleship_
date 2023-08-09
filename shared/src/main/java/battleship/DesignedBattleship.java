package battleship;

import java.io.Serializable;
import java.util.HashMap;

public class DesignedBattleship <T> extends BasicShip<T> implements Serializable {
    Placement place;

    ShipDisplayInfo<T> myDisplayInfo;
    final String name; //the name for the ship



    static HashMap<Integer, Coordinate> orderShipCoordinates;
//    public static HashMap<Integer, Coordinate> getOrderShipCoordinates() {
//        return orderShipCoordinates;
//    }


//    public DesignedBattleship(String name, Placement place,
//                         ShipDisplayInfo<T> tShipDisplayInfoSelf, ShipDisplayInfo<T> tShipDisplayInfoEnemy) {
//        super(makeBattleshipCoords(place), tShipDisplayInfoSelf, tShipDisplayInfoEnemy);
//        this.name = name;
//        this.orderShipCoordinates = new HashMap<>();
//    }
    public DesignedBattleship(String name, Placement place,
                         ShipDisplayInfo<T> tShipDisplayInfoSelf, ShipDisplayInfo<T> tShipDisplayInfoEnemy) {
        super(makeBattleshipCoords(place), tShipDisplayInfoSelf, tShipDisplayInfoEnemy);
        this.name = name;
        this.orderShipCoordinates = new HashMap<>();
    }

    /**
     *  In the order of self(data if not hit, onHit(*) if hit) and enemy(null if not hit, data if hit)
     * @param name
     * @param place include UDLR information
     * @param data
     * @param onHit
     */
    public DesignedBattleship(String name, Placement place, T data, T onHit) {
        this(name, place, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

//
//    public DesignedBattleship(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
//        super(where, myDisplayInfo, enemyDisplayInfo);
//    }

    @Override
    public String getName() {
        return name;
    }

//    static void doAddElement(HashSet<Coordinate> ans, Coordinate c, int cnt){
//        ans.add(c);
//        orderShipCoordinates.put(cnt, c);
//    }

//    /**
//     * This method DIYs battleship shapes. Since all basic elements are rectangle, I reused the rectangle create method
//     * Creating Battleships
//     * @param upperLeft_info it contains the upperLeft info as well as UDLR to generate new ship based on input
////     * @param width, w2 splits all designed ships into small rectangles(two sub-rectangles) and w1, w2 are their widths
////     * @param height, h2 splits all designed ships into small rectangles(two sub-rectangles) and w1, w2 are their heights
//     * @return the set of Coordinates that contains all the locations of
//     */
//    static HashSet<Coordinate> makeBattleshipCoords(Placement upperLeft_info){
//        HashSet<Coordinate> ans = new HashSet<>();
//        Coordinate upperLeft = upperLeft_info.getWhere();
//        int row = upperLeft.getRow();
//        int col = upperLeft.getCol();
//        Character orientation = upperLeft_info.getOrientation();
//        if(orientation == 'U'){
//            doAddElement(ans, new Coordinate(row, col + 1), 1);
//            doAddElement(ans, new Coordinate(row + 1, col), 2);
//            doAddElement(ans, new Coordinate(row + 1, col + 1), 3);
//            doAddElement(ans, new Coordinate(row + 1, col + 2), 4);
//        }
//        if(orientation == 'R') {
//            doAddElement(ans, new Coordinate(row + 1, col + 1), 1);
//            doAddElement(ans, new Coordinate(row, col), 2);
//            doAddElement(ans, new Coordinate(row + 1, col), 3);
//            doAddElement(ans, new Coordinate(row + 2, col), 4);
//        }
//        if(orientation == 'D'){
//            doAddElement(ans, new Coordinate(row + 1, col + 1), 1);
//            doAddElement(ans, new Coordinate(row, col + 2), 2);
//            doAddElement(ans, new Coordinate(row, col + 1), 3);
//            doAddElement(ans, new Coordinate(row, col), 4);
//        }
//        if(orientation == 'L'){
//            doAddElement(ans, new Coordinate(row + 1, col), 1);
//            doAddElement(ans, new Coordinate(row + 2, col + 1), 2);
//            doAddElement(ans, new Coordinate(row + 1, col + 1), 3);
//            doAddElement(ans, new Coordinate(row, col + 1), 4);
//        }
//        return ans;
//    }
    /**
     * This method DIYs battleship shapes. Since all basic elements are rectangle, I reused the rectangle create method
     * Creating Battleships
     * @param upperLeft_info it contains the upperLeft info as well as UDLR to generate new ship based on input
    //     * @param width, w2 splits all designed ships into small rectangles(two sub-rectangles) and w1, w2 are their widths
    //     * @param height, h2 splits all designed ships into small rectangles(two sub-rectangles) and w1, w2 are their heights
     * @return the set of Coordinates that contains all the locations of
     */
//    static HashSet<Coordinate> makeBattleshipCoords(Placement upperLeft_info){
//        HashSet<Coordinate> ans = new HashSet<>();
//        Coordinate upperLeft = upperLeft_info.getWhere();
//        int row = upperLeft.getRow();
//        int col = upperLeft.getCol();
//        Character orientation = upperLeft_info.getOrientation();
//        if(orientation == 'U'){
//            ans.add(new Coordinate(row, col + 1));
//            ans.add(new Coordinate(row+1, col));
//            ans.add(new Coordinate(row+1, col + 1));
//            ans.add(new Coordinate(row+1, col + 2));
//        }
//        if(orientation == 'R') {
//            ans.add(new Coordinate(row, col));
//            ans.add(new Coordinate(row + 1, col));
//            ans.add(new Coordinate(row + 1, col + 1));
//            ans.add(new Coordinate(row + 2, col));
//        }
//        if(orientation == 'D'){
//            ans.add(new Coordinate(row, col));
//            ans.add(new Coordinate(row, col + 1));
//            ans.add(new Coordinate(row, col + 2));
//            ans.add(new Coordinate(row+1, col + 1));
//        }
//        if(orientation == 'L'){
//            ans.add(new Coordinate(row, col + 1));
//            ans.add(new Coordinate(row+1, col));
//            ans.add(new Coordinate(row+1, col + 1));
//            ans.add(new Coordinate(row+2, col + 1));
//        }
//        return ans;
//    }

    /**
     * For moving, it needs to be record the order!
     * @param upperLeft_info
     * @return
     */
    static HashMap<Coordinate, Integer> makeBattleshipCoords(Placement upperLeft_info){
        HashMap<Coordinate, Integer> ans = new HashMap<Coordinate, Integer>();
        Coordinate upperLeft = upperLeft_info.getWhere();
        int row = upperLeft.getRow();
        int col = upperLeft.getCol();
        Character orientation = upperLeft_info.getOrientation();
        if(orientation == 'U'){
            ans.put(new Coordinate(row, col + 1), 1);
            ans.put(new Coordinate(row + 1, col), 2);
            ans.put(new Coordinate(row + 1, col + 1), 3);
            ans.put(new Coordinate(row + 1, col + 2), 4);
        }
        if(orientation == 'R') {
            ans.put(new Coordinate(row + 1, col + 1), 1);
            ans.put(new Coordinate(row, col), 2);
            ans.put(new Coordinate(row + 1, col), 3);
            ans.put(new Coordinate(row + 2, col), 4);
        }
        if(orientation == 'D'){
            ans.put(new Coordinate(row + 1, col + 1), 1);
            ans.put(new Coordinate(row, col + 2), 2);
            ans.put(new Coordinate(row, col + 1), 3);
            ans.put(new Coordinate(row, col), 4);
        }
        if(orientation == 'L'){
            ans.put(new Coordinate(row + 1, col), 1);
            ans.put(new Coordinate(row + 2, col + 1), 2);
            ans.put(new Coordinate(row + 1, col + 1), 3);
            ans.put(new Coordinate(row, col + 1), 4);
        }
        return ans;
    }
}
