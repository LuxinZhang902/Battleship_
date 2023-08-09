package battleship;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Constructs a BattleShipBoard with the specified width
 * and height
 * @throws IllegalArgumentException if the width or height are less than or equal to zero.
*/
public interface Board<T> {
  public int getWidth();
  public int getHeight();
//  private final ArrayList<Ship<T>> myShips;

  public String tryAddShip(Ship<T> toAdd);
//  public boolean tryAddShip(Ship<T> toAdd);

  public T whatIsAtForSelf(Coordinate where);

  public Ship<T> fireAt(Coordinate c);
//  protected T whatIsAt(Coordinate where, boolean isSelf);

  public T whatIsAtForEnemy(Coordinate where);
//  public boolean isLose();
  public boolean isAllShipsSunk();
  public String removeShip(Ship<T> toRemove);
  public Ship<T> getShip(Coordinate where);
  public HashMap<String, Integer> getEachShipSonaredNum(Coordinate center, ArrayList<Coordinate> scanned);
  public ArrayList<Coordinate> addScanRegion(Coordinate center);
}
