package battleship;

import java.io.Serializable;

public class Action implements Serializable {

    String actionType;
    Coordinate source; //For move, it is the move place it choose. For fire and sonar, it is the action destination

    Placement moveTo; //The placement for the move action



    int remainActions;

    public Action(String _actionType, Coordinate _source, Placement _moveTo, int _remainActions){
        this.actionType = _actionType;
        this.source = _source;
        this.moveTo = _moveTo;
        this.remainActions = _remainActions;
    }

    /**
     * This is the default constructor that does nothing. It is used to initialize the Action
     */
    public Action() {

    }


    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Coordinate getSource() {
        return source;
    }

    public void setSource(Coordinate source) {
        this.source = source;
    }

    public Placement getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(Placement moveTo) {
        this.moveTo = moveTo;
    }

    public int getRemainActions() {
        return remainActions;
    }

    public void reduceActionNum(){
        remainActions --;
    }

}
