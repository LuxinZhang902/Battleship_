package battleship;

import java.io.Serializable;

/**
 * This class places a ship with its starting position(represented using Coordinate class)
 * Also, its orientation (H or V) to put it horizontally or vertically
 * It has a constructor takes a string and rephrase it into the (x, y) and its orientation
 * It has getter methods, toString, equals, hashCode methods
 */
public class Placement implements Serializable {
    public final Coordinate where;
    public final char orientation;
//
//    public boolean equals(Object object) {
//        if (this == object) return true;
//        if (object == null || getClass() != object.getClass()) return false;
//        if (!super.equals(object)) return false;
//        Placement placement = (Placement) object;
//        return orientation == placement.orientation && java.util.Objects.equals(where, placement.where);
//    }
//
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), wherProvide a place that you want to move the ship to(i.e. A0V, B0L):e, orientation);
//    }

    /**
     * This constructor takes a Coordinate and a char to pass into the fields in Placement class
     * Adding UDLR in version 2
     * @param where: a Coordinate
     * @param orientation: a char
     */
    public Placement(Coordinate where, char orientation) {
        this.where = where;
        //To make sure when using this constructor, the lower and upper case can still be solved
        if(orientation == 'H' || orientation == 'V'|| orientation == 'U' || orientation == 'D'
                || orientation == 'L' || orientation =='R') {
            this.orientation = orientation;
        }
        //97 - 65 = 32. 32 is a space
        else if(orientation == 'h') {
            this.orientation = 'H';
        }
        else if(orientation == 'v'){
            this.orientation = 'V';
        }
        else if(orientation == 'u'){
            this.orientation = 'U';
        }
        else if(orientation == 'd'){
            this.orientation = 'D';
        }
        else if(orientation == 'l'){
            this.orientation = 'L';
        }
        else if(orientation == 'r'){
            this.orientation = 'R';
        }
//        else{
//            throw new IllegalArgumentException("Placement's orientation is incorrect!\n");
//        }
        else{
            this.orientation = orientation;
        }

    }

    /**
     * This Constructor takes in a string and transfer it into the row and col
     * @param place is a string
     * i.e. "A0V": row = 0, col = 2, orientation: 'V'
     */
    public Placement(String place) {
        place = place.toUpperCase();
//        place = place.toUpperCase(Locale.ROOT);
        //There should be exactly 3 in length, otherwise, throw exceptions
        if(place.length() != 3){
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }

        String descr = place.substring(0, 2); //substring of [0, 2)
        this.where = new Coordinate(descr);

        char orientation = place.charAt(2);
        //Left, Right, Down, Up, Horizontal, Vertical
//        if(orientation  == 'U' || orientation == 'D' || orientation  == 'L' || orientation == 'R'
//                || orientation  == 'H' || orientation == 'V' ) {
        if(orientation  == 'H' || orientation == 'V' || orientation == 'U' || orientation == 'D'
            || orientation == 'L' || orientation =='R') {
            this.orientation = orientation;
        }
        else {
//            throw new IllegalArgumentException("orientation should be U, D, L, or R, but is: " + orientation + "\n");
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
//            throw new IllegalArgumentException("orientation should be H or V, but is: " + orientation + "\n");
        }


    }


    public Coordinate getWhere() {
        return where;
    }

    public char getOrientation() {
        return orientation;
    }


    /**
     * This method checks if two objects are equal
     * @param  o is inherited it from Object
     * @return true if two objects are equal, false if two objects are not equal
     */
    @Override
    public boolean equals(Object o) {
        //It requires that o has *exactly* the same class as "this" object
        if (o.getClass().equals(getClass())) {
            Placement c = (Placement) o;
            return where.equals(c.where) && orientation == c.orientation;
//            return where == c.where && orientation == c.orientation;
        }
        return false;
    }

    /**
     * This toString method reuses the toString() method in the Coordinate class
     * @return the string that contains the where and orientation
     */
    @Override
    public String toString(){
        return "(" + where.toString() + ", " + orientation + ")";
    }

    /**
     * This method gets the hash code
     * @return a hash value of the string
     * For later put it into the mapping structures
     */
    @Override
    public int hashCode(){
        return toString().hashCode();
    }

}
