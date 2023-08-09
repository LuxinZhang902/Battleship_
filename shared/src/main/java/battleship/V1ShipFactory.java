package battleship;

import java.io.Serializable;

public class V1ShipFactory implements AbstractShipFactory<Character>, Serializable {
    /**
     * This works as a helper function to create each ship
     * @param where Placement, define the coordinate and its orientation
     * @param w the width of this ship
     * @param h the height of this ship
     * @param letter the representing letter of this ship
     * @param name the name of this ship(since it is needed to use as the information to show to the user)
     * @return a Rectangle ship TODO use other ships to substitute later
     */
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name){
        Coordinate c = where.getWhere();
        char orientation = where.getOrientation();
        //For checking vertical and horizontal
        int width = w;
        int height = h;
        if(orientation == 'H'){
            width = h;
            height = w;
        }
        //public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit)
        return new RectangleShip<Character>(name, c, width, height, letter, '*');
    }

    @Override
    public Ship<Character> makeSubmarine(Placement where) {
        return createShip(where, 1, 2, 's', "Submarine");
    }

    @Override
    public Ship<Character> makeBattleship(Placement where) {
        return createShip(where, 1, 4, 'b', "Battleship");
    }

    @Override
    public Ship<Character> makeCarrier(Placement where) {
        return createShip(where, 1, 6, 'c', "Carrier");
    }

    @Override
    public Ship<Character> makeDestroyer(Placement where) {
        return createShip(where, 1, 3, 'd', "Destroyer");
    }
}
