package battleship;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T> implements Serializable {
    Coordinate upperLeft;
    int width;
    int height;
    ShipDisplayInfo<T> myDisplayInfo;
    final String name; //the name for the ship

    final int isTopLeftNull = -1;
    public int isTopLeftNull() {
        return isTopLeftNull;
    }


    //3 constructors for the RectangleShip
//    public RectangleShip(String name, Coordinate upperLeft, int width, int height,
//                         ShipDisplayInfo<T> tShipDisplayInfoSelf, ShipDisplayInfo<T> tShipDisplayInfoEnemy) {
//        super(makeCoords(upperLeft, width, height), tShipDisplayInfoSelf, tShipDisplayInfoEnemy);
//        this.name = name;
//    }
    public RectangleShip(String name, Coordinate upperLeft, int width, int height,
                         ShipDisplayInfo<T> tShipDisplayInfoSelf, ShipDisplayInfo<T> tShipDisplayInfoEnemy) {
        super(makeCoords_new(upperLeft, width, height), tShipDisplayInfoSelf, tShipDisplayInfoEnemy);
        this.name = name;
    }

    /**
     *  In the order of self(data if not hit, onHit(*) if hit) and enemy(null if not hit, data if hit)
     * @param name
     * @param upperLeft
     * @param width
     * @param height
     * @param data
     * @param onHit
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
        this(name, upperLeft, width, height,
                new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

    /**
     *  the last constructor is just a convience constructor to avoid changes in our code
     * as we change the behavior from "Always 1x1" to "width x height".  Since we only use that in testing,
     * we'll just put "testship" for the name
     * @param upperLeft
     * @param data
     * @param onHit
     */
    public RectangleShip(Coordinate upperLeft, T data, T onHit) {
        this("testship", upperLeft, 1, 1, data, onHit);
    }
    //added
    public RectangleShip(Coordinate upperLeft, int width, int heitgh, T data, T onHit) {
        this("testship", upperLeft, width, heitgh, data, onHit);
    }


    /**
     * This method generates the set of coordinates for a rectangle starting at upperLeft
     * whose width and height are as specified.
     * i.e. upperLeft = (row=1, col=2)
     * width = 1, height = 3
     * @param upperLeft
     * @param width: the width of the rectangle ship
     * @param height
     * @return the set {(row=1,col=2), (row=2,col=2), (row=3,col=2)}
     */
    static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height){
        HashSet<Coordinate> ans = new HashSet<>();
        int startRow = upperLeft.getRow();
        int startCol = upperLeft.getCol();
//        System.out.println("start row is: "+ startRow);
//        System.out.println("start col is: "+ startCol);

        for(int i = startRow; i < startRow + height; i++){
            for(int j = startCol; j < startCol + width; j++){
                Coordinate curr = new Coordinate(i, j);
                //System.out.println("coordinate is:" + curr.toString());
                ans.add(curr);
            }
        }
        return ans;
    }

    static HashMap<Coordinate, Integer> makeCoords_new(Coordinate upperLeft, int width, int height){
        HashMap<Coordinate, Integer> ans = new HashMap<Coordinate, Integer>();
        int startRow = upperLeft.getRow();
        int startCol = upperLeft.getCol();
//        System.out.println("start row is: "+ startRow);
//        System.out.println("start col is: "+ startCol);
        int cnt = 1;
        for(int i = startRow; i < startRow + height; i++){
            for(int j = startCol; j < startCol + width; j++){
                Coordinate curr = new Coordinate(i, j);
                //System.out.println("coordinate is:" + curr.toString());
                ans.put(curr, cnt);
                cnt ++;
            }
        }
        return ans;
    }

    @Override
    public String getName() {
        return name;
    }

//
//    @Override
//    public Iterable<Coordinate> getCoordinates() {
//        return myPieces.keySet();
//    }
}
