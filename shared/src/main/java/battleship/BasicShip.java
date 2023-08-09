package battleship;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public abstract class BasicShip<T> implements Ship<T>, Serializable {

    protected ShipDisplayInfo<T> myDisplayInfo;

    /**
     * if myPieces.get(c)  is null, c is not part of this Ship
     * if myPieces.get(c)  is false, c is part of this ship and has not been hit
     * if myPieces.get(c)  is true, c is part of this ship and has been hit
     */
    protected HashMap<Coordinate, Boolean> myPieces;
    protected ShipDisplayInfo<T> enemyDisplayInfo;

    protected HashMap<Coordinate, T> whatIsAtShip;
    protected HashMap<Coordinate, Boolean> scannedPieces;

    protected HashMap<Coordinate, Integer> coordinate_ordered;
    public HashMap<Coordinate, Integer> getCoordinate_ordered() {
        return coordinate_ordered;
    }



//    abstract HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height);
//    upperLeft, width, height


//    public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
//        this.myPieces = new HashMap<>();
//        for (Coordinate c : where) {
//            myPieces.put(c, false);
////            this.myDisplayInfo = myDisplayInfo;
//        }
//        this.myDisplayInfo = myDisplayInfo;
//        this.enemyDisplayInfo = enemyDisplayInfo;
//    }
    public BasicShip(HashMap<Coordinate, Integer> coordinates_ordered,
                     ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        this.myPieces = new HashMap<>();
        Iterable<Coordinate> where = coordinates_ordered.keySet();
        for (Coordinate c : where) {
            myPieces.put(c, false);
//            this.myDisplayInfo = myDisplayInfo;
        }
        this.myDisplayInfo = myDisplayInfo;
        this.enemyDisplayInfo = enemyDisplayInfo;
        this.coordinate_ordered = coordinates_ordered;
    }


    /**
     * This constructor contains where the ship starts to locate and the shape of the ship.
     * @param myLocation
     * @param shape: s(submarines), d(destroyers), b(battleship), c(carrier)
     */
//    public BasicShip(Coordinate myLocation, char shape) {
//        this.myLocation = myLocation;
//        this.shape = shape;
//    }


    /**
     * Check if this ship occupies the given coordinate.
     *
     * @param where is the Coordinate to check if this Ship occupies
     * @return true if where is inside this ship, false if not.
     */
    @Override
    public boolean occupiesCoordinates(Coordinate where) {
        for (Coordinate c : myPieces.keySet()) {
            if (where.equals(c)) {
                return true;
            }
        }
        return false;

    }
    /**
     * Check if this ship occupies the given coordinate.
     *
     * @param where is the Coordinate to check if this Ship occupies
     * @return 1 if there has the element(true), 0 if there does not have an element
     */
//    @Override
//    public int occupiesCoordinates(Coordinate where) {
//        for (Coordinate c : myPieces.keySet()) {
//            if (where.equals(c)) {
//                return 1;
//            }
//        }
//        return false;
//
//    }

    /**
     * Check if this ship has been hit in **all** of its locations meaning it has been
     * sunk.
     *
     * @return true if this ship has been sunk, false otherwise.
     */
    @Override
    public boolean isSunk() {
        int size = myPieces.size();
        int i = 0;
        for (Coordinate c : myPieces.keySet()) {
            if (myPieces.get(c)) {
                ++i;
            }
        }
        //only when all places in myPieces are true can the ship sunk
        if (i == size) {
            return true;
        }
        return false;

    }

    /**
     * Make this ship record that it has been hit at the given coordinate. The
     * specified coordinate must be part of the ship.
     *
     * @param where specifies the coordinates that were hit.
     * @throws IllegalArgumentException if where is not part of the Ship
     */
    @Override
    public void recordHitAt(Coordinate where) {
        //Checking whether this position has the ship
        checkCoordinateInThisShip(where);
        //Since true means it was hit
        myPieces.put(where, true);
    }

    /**
     * Check if this ship was hit at the specified coordinates. The coordinates must
     * be part of this Ship.
     *
     * @param where is the coordinates to check.
     * @param  isScanned 1: is scanned, all return true. 0: is not scanned, returs depending on whether inside myPieces
     * @return true if this ship as hit at the indicated coordinates, and false
     * otherwise.
     * @throws IllegalArgumentException if the coordinates are not part of this
     *                                  ship.
     */
    @Override
    public boolean wasHitAt(Coordinate where, int isScanned) {
//        //If it is scanned, return true(which means was hit)
//        if(isScanned == 1){
//            return true;
//        }
        //Checking whether this position has the ship
        checkCoordinateInThisShip(where);
//        if(myPieces.get(where) == null){
//            return false;
//        }
        //If the ship was hit
        if (myPieces.get(where) == true) {
            return true;
        }
        return false;
    }
//
//    public boolean wasScanned(Coordinate where){
//        if (scannedPieces.get(where) == true) {
//            return true;
//        }
//        return false;
//    }

    /**
     * Return the view-specific information at the given coordinate. This coordinate
     * must be part of the ship.
     *
     * @param where is the coordinate to return information for
     * @param myShip is used to decide whether to use myDisplayInfo o enemyDisplayInfo to call getInfo on
     *               myShip is true: self; myShip is false: enemy
     * @return The view-specific information at that coordinate.
     * @throws IllegalArgumentException if where is not part of the Ship
     */

    @Override
    public T getDisplayInfoAt(Coordinate where, boolean myShip, int isScanned) {
        //TODO this is not right.  We need to
        //look up the hit status of this coordinate
//        try{
//            checkCoordinateInThisShip(where);
//        }
//        catch (IllegalArgumentException e){
//            return null;
//        }
        //If the action is not scan, check is coordinate in this ship. else, don't check it!
//        if(isScanned == 0){
            checkCoordinateInThisShip(where);
//        }


        if(myShip){
            if (wasHitAt(where, isScanned)) {
                return myDisplayInfo.getInfo(where, true); //*
            } else {
                return myDisplayInfo.getInfo(where, false);//s, d, c, b
            }
        }
        else{
            if (wasHitAt(where, isScanned)) {
                return enemyDisplayInfo.getInfo(where, true);
            } else {
                return enemyDisplayInfo.getInfo(where, false);
            }
        }


    }

//    @Override
//    public T getScannedInfo(Coordinate where){
//        return myDisplayInfo.getInfo(where, true);
//    }
//
//    @Override
//    public T getScannedCenter(T letter){
//        return letter;
//    }

    /**
     * This method checks if c is part of this ship(in myPieces)
     * If not, throw an IllegalArgumentException
     *
     * @param c
     */
    protected void checkCoordinateInThisShip(Coordinate c) {
        int isPartOfShip = 0;
        Iterator<Map.Entry<Coordinate, Boolean>> it = myPieces.entrySet().iterator();
        //Going through all the elements in teh myPieces
        while (it.hasNext()) {
          Map.Entry<Coordinate, Boolean> pair = (Map.Entry<Coordinate, Boolean>) it.next();
            if (c.equals(pair.getKey())) {
                isPartOfShip = 1;
//                break;
            }
        }
        if (isPartOfShip == 0) {
            throw new IllegalArgumentException("the input Coordinate is not a part of this ship!");
        }

    }
    @Override
    public Iterable<Coordinate> getCoordinates() {
        return myPieces.keySet();
    }
    
//    /**
//     * This method gets the ship name by its coordinate.
//     * If this coordinate is a part of th
//     * @param where
//     * @return
//     */
//    public String getShipNameByCoor(Coordinate where){
//        return null;
//    }

    /**
     * This method gets the current status of the ship in respond of the coordinate
     * and store it into a map
     * @return
     */
//    public HashMap<Coordinate, T> getWhatIsAtShip(){
//        HashMap<Coordinate, T> map;
//        return map;
//    }

    /**
     * This method gets the upper left block of a ship.
     * Pay attention that the upper left conor does not need to have something in!
     * it just needs to record the transition position
     * @return
     */
//    public Coordinate getUpperLeft(Ship<T> ship){
//        Coordinate ans = null;
//        int minRow = Integer.MAX_VALUE;
//        int minCol = Integer.MAX_VALUE;
////        Iterator<Map.Entry<Coordinate, Boolean>> it = myPieces.entrySet().iterator();
////        //Going through all the elements in teh myPieces
////        while (it.hasNext()) {
////            Map.Entry<Coordinate, Boolean> pair = (Map.Entry<Coordinate, Boolean>) it.next();
////            return pair.getKey();
////        }
//        for (Coordinate c : myPieces.keySet()) {
//            int curRow = c.getRow();
//            int curCol = c.getCol();
//            if(curRow < minRow){
//                minRow = curRow;
//                if(minCol < curCol){
//                    curCol = minCol;
//                }
//                Coordinate c1 = new Coordinate(minRow, curCol);
//                ans = c1;
////                if(myPieces.containsKey(c1)){
////                    ans = c1;
////                }
//            }
//            if(curCol < minCol){
//                minCol = curCol;
//                Coordinate c2 = new Coordinate(minRow, minCol);
////                if(myPieces.containsKey(c2)){
////                    ans = c2;
////                }
//                ans = c2;
//
//            }
//        }
//        return ans;
//    }
    public HashMap<Coordinate, Boolean> getMyPieces(){
        return myPieces;
    }


    /**
     * This method moves a ship directly by changing the coordinate to the directed coordinate
     * Also check whether there are collisions or out-of-bound issues
     *
     * This method matches the ship status inside the ship. It works as a map
//     * @param ship the new ship added on the board
     */
//    public void moveShip(Ship<T> ship_moveFrom, Ship<Character> ship_moveTo, Coordinate from , Coordinate to){
//        //Getting the upper Left coordinate of the ship(min row and min col)(iterate)
//        Coordinate upperLeft = getUpperLeft(ship_moveFrom); //It should be valid and inside the board.
//        int fromRow = upperLeft.getRow();
//        int fromCol = upperLeft.getCol();
//        //get the to coordinate and get the mapping from from's upper left to the to location
//        int toRow = to.getRow();
//        int toCol = to.getCol();
//
//        int rowMove = Math.abs(fromRow - toRow);
//        int colMove = Math.abs(fromCol - toCol);
////          for (Coordinate c : myPieces.keySet()) {
////            Coordinate newPlace = new Coordinate(c.getRow() + rowMove, c.getCol() + colMove);
////            //If the moving
////            if(newPieces.containsKey(c)){
////                newPieces.put(newPlace, myPieces.get(c));
////            }
////         }
//
//        HashMap<Coordinate, Boolean> oldPieces = ship_moveFrom.getMyPieces();
//        HashMap<Coordinate, Boolean> newPieces = ship_moveTo.getMyPieces();
//        for (Coordinate c : oldPieces.keySet()) {
//            Coordinate newPlace = new Coordinate(c.getRow() + rowMove, c.getCol() + colMove);
//            //If the moving
//            if(newPieces.containsKey(newPlace)){
//                newPieces.put(newPlace, oldPieces.get(c));
//            }
//        }
//    }

    /**
     * This method updates the new pieces based on the corresponding number for each piece
     * @param ship_moveFrom
     * @param ship_moveTo
     */
    public void moveShip(Ship<T> ship_moveFrom, Ship<Character> ship_moveTo){
        HashMap<Coordinate, Integer> oldPieces = ship_moveFrom.getCoordinate_ordered();
        HashMap<Coordinate, Integer> newPieces = ship_moveTo.getCoordinate_ordered();
        for (Coordinate c_old : oldPieces.keySet()) {
            int oldVal = oldPieces.get(c_old);
            for(Coordinate c_new : newPieces.keySet()){
                int newVal = newPieces.get(c_new);
                if(oldVal == newVal){
                    //Record the previous coordinate's state
                    myPieces.put(c_new, ship_moveFrom.wasHitAt(c_old, 0));
                }
            }
        }
    }

    /**
     * This method sets the cooresponding piece to true
     * @param c
     */
//    @Override
//    public void setMyPiecesToTrue(Coordinate c){
//        myPieces.put(c, true);
//    }
//
}

