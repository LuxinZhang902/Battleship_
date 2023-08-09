package battleship;

import java.io.Serializable;

public class V2ShipFactory implements AbstractShipFactory<Character>, Serializable {
    /**
     * This works as a helper function to create each ship
     *
     * @param where  Placement, define the coordinate and its orientation
     * @param w      the width of this ship
     * @param h      the height of this ship
     * @param letter the representing letter of this ship
     * @param name   the name of this ship(since it is needed to use as the information to show to the user)
     * @return a Rectangle ship TODO use other ships to substitute later
     */
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
        Coordinate c = where.getWhere();
        char orientation = where.getOrientation();
        //For checking vertical and horizontal
        int width = w;
        int height = h;
        if (orientation == 'H') {
            width = h;
            height = w;
        }
        //public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit)
        return new RectangleShip<Character>(name, c, width, height, letter, '*');
    }

    /**
     * This method is for the newly designed battleship
     * @param where
     * @param letter
     * @param name
     * @return
     */
    protected Ship<Character> createDesignedBattleship(Placement where, char letter, String name) {
        //public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit)
        return new DesignedBattleship<Character>(name, where, letter, '*');
    }


    /**
     * This method is for the newly designed carrier ships
     * @param where
     * @param letter
     * @param name
     * @return
     */
    protected Ship<Character> createDesignedCarrierShip(Placement where, char letter, String name) {
        //public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit)
        return new DesignedCarrierShip<Character>(name, where, letter, '*');
    }


    //Remain unchanged, keep it as in version 1
    @Override
    public Ship<Character> makeSubmarine(Placement where) {
        return createShip(where, 1, 2, 's', "Submarine");
    }

    @Override
    public Ship<Character> makeDestroyer(Placement where) {
        return createShip(where, 1, 3, 'd', "Destroyer");
    }

    //get new shapes
    @Override
    public Ship<Character> makeBattleship(Placement where) {
        return createDesignedBattleship(where, 'b', "Battleship");
    }

    @Override
    public Ship<Character> makeCarrier(Placement where) {
        return createDesignedCarrierShip(where,  'c', "Carrier");
    }


}
