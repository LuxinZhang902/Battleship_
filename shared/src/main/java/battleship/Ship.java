package battleship;

import java.util.HashMap;

/**
 * This interface represents any type of Ship in our Battleship game. It is
 * generic in typename T, which is the type of information the view needs to
 * display this ship.
 */
public interface Ship<T> {
    /**
     * Check if this ship occupies the given coordinate.
     *
     * @param where is the Coordinate to check if this Ship occupies
     * @return true if where is inside this ship, false if not.
     */
    public boolean occupiesCoordinates(Coordinate where);

    /**
     * Check if this ship has been hit in all of its locations meaning it has been
     * sunk.
     *
     * @return true if this ship has been sunk, false otherwise.
     */
    public boolean isSunk();

    /**
     * Make this ship record that it has been hit at the given coordinate. The
     * specified coordinate must be part of the ship.
     *
     * @param where specifies the coordinates that were hit.
     * @throws IllegalArgumentException if where is not part of the Ship
     */
    public void recordHitAt(Coordinate where);

    /**
     * Check if this ship was hit at the specified coordinates. The coordinates must
     * be part of this Ship.
     *
     * @param where is the coordinates to check.
     * @return true if this ship as hit at the indicated coordinates, and false
     *         otherwise.
     * @throws IllegalArgumentException if the coordinates are not part of this
     *                                  ship.
     */
    public boolean wasHitAt(Coordinate where, int isScanned);

    /**
     * Return the view-specific information at the given coordinate. This coordinate
     * must be part of the ship.
     *
     * @param where is the coordinate to return information for
     * @param myShip is whether this ship is self or enemy's
     * @param isScanned 1: scanned, 0: not scanned
     * @throws IllegalArgumentException if where is not part of the Ship
     * @return The view-specific information at that coordinate.
     */
    public T getDisplayInfoAt(Coordinate where, boolean myShip, int isScanned);

    /**
     * Get the name of this Ship, such as "submarine".
     * @return the name of this ship
     */
    public String getName();

    /**
     * Get all of the Coordinates that this Ship occupies.
     * @return An Iterable with the coordinates that this Ship occupies
     */
    public Iterable<Coordinate> getCoordinates();


    public HashMap<Coordinate, Boolean> getMyPieces();
//    public void moveShip(Ship<T> ship, Ship<Character> ship_moveTo, Coordinate from , Coordinate to);

//    public T getScannedInfo(Coordinate where);
//    public T getScannedCenter(T letter);
//    public void setMyPiecesToTrue(Coordinate c);
    public HashMap<Coordinate, Integer> getCoordinate_ordered();

    public void moveShip(Ship<T> ship_moveFrom, Ship<Character> ship_moveTo);



}
