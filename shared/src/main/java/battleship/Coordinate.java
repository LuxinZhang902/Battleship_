
package battleship;

import java.io.Serializable;

/**
 * This class creates a coordinate with col and row
 * It checks whether the row and column are valid
 * It rephrases a string to its corresponding row and column
 * It has getter methods, toString, equals, hashCode methods
 *
 */
public class Coordinate implements Serializable {
    private final int col;
    private final int row;
    /**
     * This constructor takes row first, then the col
     * @param row: the number of rows
     * @param col: the number of cols
     * to match with string input's order
     */
    public Coordinate(int row, int col) {
        //Both col and row here can equal to 0 handling the error!
        if(col < 0){
            throw new IllegalArgumentException("Coordinate's column must be positive but is " + col);
        }
        if(row < 0){
            throw new IllegalArgumentException("Coordinate's row must be positive but is " + row);
        }
        this.col = col;
        this.row = row;
    }

    /**
     * This Constructor takes in a string and transfer it into the row and col
     * @param descr is a string
     * i.e. "A2": row = 0, col = 2
     */
    public Coordinate(String descr){
        descr = descr.toUpperCase(); //Transfer all letters into upper-case letter

        //There should be exactly 2 in length, otherwise, throw exceptions
        if(descr.length() != 2){
            throw new IllegalArgumentException("Coordinate's input string must be 2 but is: " + descr.length() + "\n");
        }

        int exp_row = descr.charAt(0) - 'A';
        //Only if the letter is between A and Z can it be transfered to the proper row (0-26: A - Z)
        if(descr.charAt(0) - 'A' >= 0 && descr.charAt(0) - 'T' <= 0){
            this.row = exp_row;
        }
        else{
            throw new IllegalArgumentException("Coordinate's row should be A - T but is: " + descr.charAt(0) + "\n");
        }

        int exp_col = descr.charAt(1) - '0';
        //Only if the number is between 0 and 9 can it be transfered to the proper col
        if(descr.charAt(1) - '0' >= 0 && descr.charAt(1) - '9' <= 0){
            this.col = exp_col;
        }
        else{
            throw new IllegalArgumentException("Coordinate's col should be 0 - 9 but is: " + descr.charAt(1) + "\n");
        }
    }


    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    /**
     * This method checks if two objects are equal
     * @param o is inherited it from Object
     * @return true if two objects are equal, false if two objects are not equal
     */
    @Override
    public boolean equals(Object o) {
        //It requires that o has *exactly* the same class as "this" object
        if (o.getClass().equals(getClass())) {
            Coordinate c = (Coordinate) o;
            return row == c.row && col == c.col;
        }
        return false;
    }

    /**
     * This method puts the row and column into the string
     * @return a string that contains row and column
     */
    @Override
    public String toString(){
        return "("+row+", " + col+")";
    }

    /**
     * This method gets the hash code
     * @return a hash value of the string that contains row and column
     * For later put it into the mapping structures
     */
    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    /**
     * This method is for the computer to get the corresponding string based on the coordinate
     * @return
     */
    public String getDirectString(){
        StringBuilder s = new StringBuilder();
        char r = (char) (row + 'A');
        s.append(r);
        s.append(col);
        return s.toString();
    }
}
