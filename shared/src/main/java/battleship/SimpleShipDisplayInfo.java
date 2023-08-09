package battleship;

import java.io.Serializable;

public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T>, Serializable {
    private T myData;
    private T onHit;
    private T captical;

    public T getMyData() {
        return myData;
    }

    public T getOnHit() {
        return onHit;
    }



    public SimpleShipDisplayInfo(T myData, T onHit) {
        this.myData = myData;
        this.onHit = onHit;
    }

    /**
     * This method checks if (hit) and returns onHit if so, and myData otherwise.
     * @param where
     * @param hit
     * @return
     */
    @Override
    public T getInfo(Coordinate where, boolean hit) {
        if(hit){
            return onHit;
        }
//        if(isUpperLeft == 1){
//            return captical;
//        }
        return myData;
    }
}