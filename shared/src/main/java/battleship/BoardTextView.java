package battleship;

import java.io.Serializable;
import java.util.function.Function;

/**
 * This class handles textual display of a Board
 * (i.e., converting it to a string to show to the user)
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the enemy's board.
*/
public class BoardTextView implements Serializable {


  /**
   * The Board to display
   */
  private final Board<Character> toDisplay;
  /**
   * Constructs a BoardView, given the board it will display.
   * @param toDisplay is the Board to display
   * @throws IllegalArgumentException if th eboard is larger than 10x26
   */
  public BoardTextView(Board<Character> toDisplay){
    this.toDisplay = toDisplay;
    if(toDisplay.getWidth() > 10 || toDisplay.getHeight() > 20){
      throw new IllegalArgumentException(
                                         "Board must be no larger than 10x20, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
      
    }
  }

  public String displayBoard(){
    String header = makeHeader();
    String body = makeBody();
    String s = header + body + header;
    return s;
  }


  /**
   * This method displays the empty board without passing parameters
   */
  public String displayEmptyBoard(){
    int row = toDisplay.getHeight();
    int col = toDisplay.getWidth();

    String sep = "|";
    StringBuilder s = new StringBuilder();
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'J', 'H', 'I', 'J', 'K', 'L', 'M','N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    //Here is where your 1x1 ship located at: (3, 2)
    //TODO: change the width and height here
    //TODO: the direction of the ship has not been handle yet
    s.append(makeHeader());

    //Displaying the body of the board
    for(int i = 0; i < row; i++){
      s.append(letters[i]);
      s.append(" "); //one spaces
      for(int j = 0; j < col; j++) {
        Coordinate c = new Coordinate(i, j);
          s.append(' ');
        if(j != col - 1){
          s.append(sep);
        }

      }
      s.append(" "); //two space
      s.append(letters[i]);
      s.append("\n");
    }
    s.append(makeHeader());
    return s.toString();
  }

  public String displayMyOwnBoard(){
    return displayAnyBoard((c)->toDisplay.whatIsAtForSelf(c));
  }
  public String displayEnemyBoard(){
    return displayAnyBoard((c)->toDisplay.whatIsAtForEnemy(c));
  }

  /**
   * This method is to display any board ignoring whether it is self board or enemy board
   * @return
   */
  protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn){
    int row = toDisplay.getHeight();
    int col = toDisplay.getWidth();

    String sep = "|";
    StringBuilder s = new StringBuilder();
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M','N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    s.append(makeHeader());

    //Displaying the body of the board
    for(int i = 0; i < row; i++){
      s.append(letters[i]);
      s.append(" "); //one spaces
      for(int j = 0; j < col; j++) {
        Coordinate c = new Coordinate(i, j);
        if(getSquareFn.apply(c) == null){
          s.append(' ');
        }
        else{
          s.append(getSquareFn.apply(c));
        }
        if(j != col - 1){
          s.append(sep);
        }

      }
      s.append(" "); //two space
      s.append(letters[i]);
      s.append("\n");
    }
    s.append(makeHeader());
    return s.toString();
  }

  /**
   * This method makes the header line, e.g. 0|1|2|3|4\n
   *
   * @return the String that is the header line for the given board
   */
  public String makeHeader(){
    StringBuilder ans = new StringBuilder("  ");
    String sep = "";
    for(int i = 0; i < toDisplay.getWidth(); i++){
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }

  public String makeBody(){
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M','N',
                      'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    String sep = "|";   
    StringBuilder s = new StringBuilder("");
    int row = toDisplay.getHeight();
    int col = toDisplay.getWidth();

    //Displaying the body of the board
    for(int i = 0; i < row; i++) {
      s.append(letters[i]);
      s.append(" "); //one spaces
      for (int j = 0; j < col - 1; j++) {
        s.append(" ");
        s.append(sep);
      }
      s.append("  "); //two space
      s.append(letters[i]);
      s.append("\n");
    }
    return s.toString();
  }

  /**
   * This method displays two boards, with myOwnBoard in the left and the Enemy's in the right
   * //TODO check spaces in between !!!
   * @param enemyView the enemy view for the self board
   * @param myHeader the header is the board name!!!
   * @param enemyHeader the enemy's header
   * @return
   */
  public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader){
    String myBoard = displayMyOwnBoard();
    String enemyBoard = enemyView.displayEnemyBoard();

    StringBuilder s = new StringBuilder();
    //TODO delete it!
    s.append("\n");
    StringBuilder head_combine = new StringBuilder();
    int selfHeaderLen = myHeader.length();
//    int enemyHeaderLen = enemyHeader.length();

    //The header (for board names)
    //The beginning should be 5 spaces
    for(int i = 0; i < 5; i++){
      head_combine.append(" "); //once space at a time
    }
    head_combine.append(myHeader); //Self Header:

    //Adding space
    //The second board starts at 2 * w + 22
    //The first board length is selfHeaderLen and there is 5 spaces
    //Thus, there will be (2 * w + 22 - (selfHeaderLen + 5) spaces inside)
    int w = toDisplay.getWidth();
    //Repeat the space in the middle
//    System.out.println("#of space: " + (2 * w + 17 - selfHeaderLen));
    for(int i = 0; i < 2 * w + 17 - selfHeaderLen; i++){
      head_combine.append(" "); //once space at a time
    }

    head_combine.append(enemyHeader); //Enemy's Header
    head_combine.append("\n"); //The next line notation
    s.append(head_combine);

    //The Body
    StringBuilder body_combine = new StringBuilder();
    String[] selfBoardLines = myBoard.split("\n"); //Strings on each line
    String[] enemyBoardLines = enemyBoard.split("\n");
    int len = selfBoardLines.length; //Both should have the same length

    //The second board starts at 2 * w + 19 i.e. w = 10: 39
    //The first board length is 2 * w + 3
    //Thus, there will be (2 * w + 19 - 2  * w - 3 = 16 spaces inside)
    int addSpace = 16;

    for(int i = 0; i < len; i++){
      body_combine.append(selfBoardLines[i]); //Include the two in spaces in the beginning
      //In the top and bottom header, two more spaces
      if(i == 0 || i == len - 1){
        //adding two spaces after 0|1|.....|9spacespace
        for(int j = 0; j < addSpace + 2; j++){
          body_combine.append(" ");
        }
      }
      else{
        for(int j = 0; j < addSpace; j++){
          body_combine.append(" "); //One Space at a time
        }
      }

      body_combine.append(enemyBoardLines[i]);
      body_combine.append("\n");
    }

    s.append(body_combine);
    return s.toString();
  }

  public Board<Character> getBattleshipBoard() {
    return toDisplay;
  }


}
