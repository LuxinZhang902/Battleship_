package battleship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class creates a BattleShipBoard.
 * with its width, height, and the current shape of the ship
 * Inside this method, it checks whether width and height are valid
 * It can add a ship into the BattleshipBoard(tryAddShip)
 * It can display (x, y)'s item (WhatIsAt)
 * @param <T>
 */
public class BattleShipBoard<T> implements Board<T>, Serializable {
  private final int width;
  private final int height;

  //Nothing outside of this class should ever know we keep our ships in an ArrayList, nor operate on it
  // No setter method:the width/height are fixed once created
  private final ArrayList<Ship<T>> myShips;
  private final ArrayList<TextPlayer> allPlayers;
  private PlacementRuleChecker<T> placementRuleChecker;
  private HashSet<Coordinate> enemyMisses; //Track where the enemy has missed
  final T missInfo;

  public HashSet<Coordinate> getEnemyMisses() {
    return enemyMisses;
  }


  //Getter methods
  public int getWidth(){
    return width;
  }
  public int getHeight(){
    return height;
  }

  //Adding one more field into this constructor
  public BattleShipBoard(int width, int height, T missInfo) {
    if(width <= 0){
      throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + width);
    }
    if(height <= 0){
      throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + height);
    }
    this.width = width;
    this.height = height;
    this.myShips = new ArrayList<Ship<T>>();
    //TODO later change it to new NoCollisionRuleChecker<>(new InBoundsRuleChecker<T>(null))
    PlacementRuleChecker<T> noCollisionRuleChecker = new NoCollisionRuleChecker<T>(null);
    this.placementRuleChecker = new InBoundsRuleChecker<T>(noCollisionRuleChecker);

    this.enemyMisses = new HashSet<>();
    this.missInfo = missInfo;
    allPlayers = new ArrayList<>();
  }


  /**
   * This method tries to add the ship into the board
   * @param toAdd
   * @return
   */
  public String tryAddShip(Ship<T> toAdd){
    //TODO: check whether it is empty or not
    String info = this.placementRuleChecker.checkPlacement(toAdd, this);
    //If there is no error in the placement
    if(info == null){
      myShips.add(toAdd);
      return null;
    }
    return info;
  }

  /**
   * This method removes a ship and checks whether it is still here
   * @param toRemove
   * @return null if it removes successfully
   */
  public String removeShip(Ship<T> toRemove){
    myShips.remove(toRemove);
    return null;
  }

  /**
   * This method is what is at for self board
   * @param where
   * @return
   */
  public T whatIsAtForSelf(Coordinate where) {
    return whatIsAt(where, true);
  }

  /**
   * This method is what is at for enemy board
   * @param where
   * @return
   */
  public T whatIsAtForEnemy(Coordinate where) {
    return whatIsAt(where, false);
  }

  /**
   * The helper method for whatIsAtSelf
   * This method takes a Coordinate, and sees which (if any) Ship
   * occupies that coordinate.  If one is found, we return whatever
   * displayInfo it has at those coordinates.  If none is found, we return null.
   * @param where
   * @return
   */
   protected T whatIsAt(Coordinate where, boolean isSelf) {
     int isScanned;
     if(isSelf){
       for (Ship<T> s: myShips) {
//         for(Coordinate scan: scannedPieces.keySet()){
//           if(s.getMyPieces().containsKey(scan)){
//             isScanned = 0;
//             return s.getDisplayInfoAt(where, isSelf, isScanned);
//           }
//           else{
////             isScanned = 1;
//             return null;
//           }
//         }
//         if (s.occupiesCoordinates(where)){
//           for(Coordinate c : s.getCoordinates()){
//             if(scannedPieces.containsKey(c)){
//               isScanned = 1;
//               return s.getDisplayInfoAt(where, isSelf, isScanned);
//             }
//             else{
//
//             }
//           }
//           isScanned = 0;
//           return s.getDisplayInfoAt(where, isSelf, isScanned);
//         }
//         if(scannedPieces.containsKey(where)){
//           for(Coordinate c : s.getCoordinates()){
//             isScanned = 0;
//             return s.getDisplayInfoAt(where, isSelf, isScanned);
//           }
////           isScanned = 1;
//////             if(scannedPieces.get(where) == true){
////////               T ch = 'C';
//////               s.getScannedCenter(ch);
//////             }
//////             isScanned = 1;
//////             As long as it was Sonar Scanned, it will be in the set
////           return s.getDisplayInfoAt(where, isSelf, isScanned);
//         }
         /*

          */
//         for(Coordinate c : scannedPieces.keySet()){
//           if(s.getMyPieces().containsKey(c)){
//             s.setMyPiecesToTrue(c);
//           }
//         }


//         If the coordinate has been occupied, return the item on it
         if (s.occupiesCoordinates(where)){
           isScanned = 0;
           return s.getDisplayInfoAt(where, isSelf, isScanned);
         }
       }
     }
     //If it is the enemy's board
     else{
       //When it is missed when fire, return missInfo
       if(enemyMisses.contains(where)){
//         System.out.println("You missed!\n");
         return missInfo;
       }
       //else, display it on the enemy board
       else{
         for (Ship<T> s: myShips) {
           //Borrowed from the ship class, the scan should be in the enemy board!
//           if(scannedPieces.containsKey(where)){
////             if(scannedPieces.get(where) == true){
//////               T ch = 'C';
////               s.getScannedCenter(ch);
////             }
////             isScanned = 1;
////             As long as it was Sonar Scanned, it will be in the set
//             return s.getScannedInfo(where);
//           }
           //If the coordinate has been occupied, return the item on it
           //TODO: else if
           if (s.occupiesCoordinates(where)){
             isScanned = 0;
             return s.getDisplayInfoAt(where, isSelf, isScanned);
           }
         }
       }
     }

    return null;
  }



  /**
   * This method Search for any ship that occupies Coordinate c,
   * If there has this Coordinate in one ship, return this ship
   * If no ships is at this coordinate,
   * record the miss in the enemyMisses and return null
   * @param c
   * @return
   */
  public Ship<T> fireAt(Coordinate c){
    //Search for any ship that occupies Coordinate c (Go through all ships)
    for(Ship<T> ship: myShips){
      //If there has this Coordinate in one ship
      if(ship.occupiesCoordinates(c)){
        //Hit it!
        ship.recordHitAt(c);
        //Return the ship
        return ship;
      }
    }
    //Record the miss in the enemyMisses is no ships is at this coordinate
    enemyMisses.add(c);
    return null;
  }

  /**
   * This method checks whether all ships have sunked,
   * if so, lose the game
   * @return true if all ships sunk and lose the game, false if not all ships sunk and has not lost the game
   */
  public boolean isAllShipsSunk(){
    int i = 0;
    for(Ship<T> ship: myShips){
      if(ship.isSunk()){
        ++i;
      }
    }
    if(i == myShips.size()){
      return true;
    }
    return false;

  }

  /**
   * Thie method gets the ship by its coordinate. It gets the ship includes the input coordinate
   * @param where the input coordinate of where the location the user chooses
   * @return
   */
  public Ship<T> getShip(Coordinate where){
    Ship<T> s = null;
    for(Ship<T> ship: myShips){
      for (Coordinate c : ship.getMyPieces().keySet()){
        //TODO check == or .equals
        if(c.equals(where)){
          s = ship;
          break;
        }
      }
    }
    return s;
  }

  /**
   * This method checks whether the coordinate is inside the board, if so, add it into the scannedRegion
   */
  public boolean addIntoSet(ArrayList<Coordinate> ans, int row, int col){
    //i.e. 10x20 (0-9) x (0-19)
//    if(row < 0 || row >= width || col < 0 || col >= height){
//      return false;
//    }
    if(row < 0 || row >= height || col < 0 || col >= width){
      return false;
    }
    ans.add(new Coordinate(row, col)); //keep false since for the enemy board, it needs to know where is hit and miss
    return true;
  }

  /**
   * This method scans the Sonar scan region and count how many squares of each type are here
   * String: the name of the ship, Integer: the number of items occupied in the scanned region
   * @param center the center of the sonar scan
   * @return
   */
  public ArrayList<Coordinate> addScanRegion(Coordinate center){
    ArrayList<Coordinate> ans = new ArrayList<>();
    //The scanned regions
    int i = center.getRow(); //The row of center
    int j = center.getCol(); //The col of center
//    HashSet<Coordinate> scannedRegion = new HashSet<Coordinate>();
    //1st line
    addIntoSet(ans, i - 3, j);

    for(int m = j - 1; m <= j + 1; m++){
      //2nd line
      addIntoSet(ans, i - 2, m);
      //6th line
      addIntoSet(ans, i + 2, m);
    }

    for(int m = j - 2; m <= j + 2; m++){
      //3rd line
      addIntoSet(ans, i - 1, m);
      //5th line
      addIntoSet(ans, i + 1, m);
    }

    //4th line
    for(int m = j - 3; m <= j + 3; m++){
        addIntoSet(ans, i, m);
    }
    addIntoSet(ans, i + 3, j);

    //get the ship, then the ship character
//    Ship<T> ship =  getShip(center);
//    String shipName = ship.getName();
    return ans;
  }

  public HashMap<String, Integer> getEachShipSonaredNum(Coordinate center, ArrayList<Coordinate> scanned){
    HashMap<String, Integer> count = new HashMap<String, Integer>();
    count.put("Submarine", 0);
    count.put("Destroyer", 0);
    count.put("Battleship", 0);
    count.put("Carrier", 0);

    for (Ship<T> s : myShips) {
      for(Coordinate shipCoor : s.getCoordinates()){
        //Using new scanned list each time
        for (int i = 0; i < scanned.size(); i++){
//        for (Coordinate c : scannedPieces.keySet()){
          if(shipCoor.equals(scanned.get(i))){
            String name = s.getName();
            //TODO check here!
            count.put(name, count.get(name) + 1);
          }
        }
      }
    }
    return count;
  }

}
