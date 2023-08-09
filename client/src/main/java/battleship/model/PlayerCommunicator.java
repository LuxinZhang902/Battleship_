/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package battleship.model;

import battleship.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;

import static java.lang.System.out;

public class PlayerCommunicator {

  private TextPlayer player;

  private final Socket socket;
  private final ObjectInputStream objectFromServer;
  private final ObjectOutputStream objectToServer;
  private BufferedReader inputReader;

  public BoardTextView getView() {
    return new BoardTextView(battleShipBoard);
  }

  private BoardTextView view;

  private int playerId = -1;
  private BattleShipBoard battleShipBoard = null; //Receive the board from the server
  final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns; //name, Map Function
  public final AbstractShipFactory<Character> shipFactory;

  int moveNum;
  int sonarNum;

  /**
   * Constructs the Client with the hostname and the port number
   * @param _hostname the host name
   * @param _portNum the port num
   * @throws IOException
   */
  public PlayerCommunicator(String _hostname, int _portNum) throws IOException {
    this.socket = new Socket(_hostname, _portNum);
    this.objectFromServer = new ObjectInputStream(socket.getInputStream());
    this.objectToServer = new ObjectOutputStream(socket.getOutputStream());
    this.inputReader = new BufferedReader(new InputStreamReader(System.in));
    this.battleShipBoard = new BattleShipBoard<Character>(10, 20, 'X');
    view = new BoardTextView(battleShipBoard);

    moveNum = 3;
    sonarNum = 3;
    shipCreationFns = new HashMap<>();
    setupShipCreationMap();
    shipFactory = new V2ShipFactory();
  }
  public int recvIntFromServer() throws IOException, ClassNotFoundException {
    playerId = (Integer )objectFromServer.readObject();
    return playerId;
  }


  public String recvStrFromServer() throws IOException, ClassNotFoundException {
    String s = (String) objectFromServer.readObject();
    return s;
  }

  /**
   * This method receives the player id from the server
   * @throws IOException
   */
  public int recvPlayerId() throws IOException {
    playerId = objectFromServer.readInt();
    System.out.println("received playerId = " + playerId + " successfully!");
    return playerId;
  }

  /**
   * This method receives the player id from the server
   * @throws IOException
   */
  public TextPlayer initPlayer() throws IOException {
    Board<Character> b = new BattleShipBoard<Character>(10, 20, 'X');

    //Initialize the BufferedReader
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    //Initialize the factory
    V2ShipFactory factory = new V2ShipFactory(); //Version 2's factory

    if(playerId == 0){
      player = new TextPlayer("A", b, input, out, factory);
    }
    else if(playerId == 1){
      player =  new TextPlayer("B", b, input, out, factory);
    }
    return player;
  }




  public void sendActionToServer(Action action) throws IOException {
    objectToServer.writeObject(action);
  }


  /**
   * This method does the placements for each player
   //   * @param v1OrV2 0: v1, 1: v2
   * @throws IOException
   */
  public void doPlacementPhrase() throws IOException {
    // TODO Adding p2.doOnePlacement later - task 16
    player.doPlacementPhase();
    battleShipBoard = (BattleShipBoard) player.getTheBoard();
  }


  /**
   * This method gets the user inputs and set it into the user's status
   * @param p1
   * @param p2
   * @throws IOException
   */
  public static void setPlayerStatus(TextPlayer p1, TextPlayer p2) throws IOException {
    out.println("Please enter the status of each player");
    int p1Status = p1.getStatus();
    int p2Status = p2.getStatus();
    p1.setIsComputer(p1Status);
    p2.setIsComputer(p2Status);

  }


  /**
   * This method closes all pipes in the client
   * @throws IOException
   */
  public void closePipes() throws IOException {
    objectFromServer.close();
    objectToServer.close();
    socket.close();
    inputReader.close();
  }

  public void sendBoardView() throws IOException {
    view = new BoardTextView(battleShipBoard);
    objectToServer.writeObject(view);
    out.println("Successfully send the boardTextView of Player" + playerId + " to the server");
  }


  /**
   * This method set up the ship.
   * Any child class that overrides these would only be able to write "pure" functions
   * They would not be able to use any parameters passed to the constructor, since the constructor
   * would not be able to put it in fields before these methods get called
   */
  protected void setupShipCreationMap(){
    //Adding
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));

  }


  public void sendString(String str) throws IOException {
    objectToServer.writeObject(str);
  }

  public int getPlayerId() {
    return playerId;
  }

  /**
   * This method overrides the toString method and gets the client's information
   * @return output String
   */
  @Override
  public String toString() {
    String output = "Client " + playerId + ": ";
    output += socket.getInetAddress();
    return output;
  }

  public void recvBoardTextView() throws IOException, ClassNotFoundException {
    String currView = (String) objectFromServer.readObject();
    out.println(currView);
  }

  /**
   * This method promote the player to enter teh action each time.
   * Sine only move action and the sonar action has counts
   * @return
   */
  public String getPrompt(){
    return " Possible actions for Player " + player.getName() + ":\n" +
            "\n" +
            " F Fire at a square\n" +
            " M Move a ship to another square (" + moveNum + " remaining)\n" +
            " S Sonar scan (" + sonarNum + " remaining)\n" +
            "\n" +
            "Player " + player.getName() + ", what would you like to do?";
  }


  /**
   * This method tries to handle the exception caused by the Coordinate
   * @param input: the input string that user gives
   * @return The error method
   */
  public String handleCoordinateException(String input){
    String res = null;
    try{
      Coordinate c = new Coordinate(input);
    }
    catch(IllegalArgumentException e){
      res = "The input coordinate is invalid, please try again!";
    }
    return res;
  }

  /**
   * This method reads the fire at coordinate or Sonar at coordinate
   * Since it acts on the enemy's board, we now have no idea whether there are ship or not in the place we are
   * trying to attack
   */
  public Coordinate readCoordinate() throws IOException {
    String sIn = inputReader.readLine();
    String info = handleCoordinateException(sIn);
    while (info != null) {
      out.println(info);
      sIn = inputReader.readLine();
      info = handleCoordinateException(sIn);
    }
    Coordinate c = new Coordinate(sIn);
    return c;
  }

  public Coordinate readMoveCoordinate() throws IOException {
    Coordinate c = readCoordinate();
    while(battleShipBoard.whatIsAtForSelf(c) == null){
      out.println("There is no ship at " + c.toString() + ", please choose another Coordinate!");
      String sIn = inputReader.readLine();
      //It purely checks whether the input coordinate is correct
      String info = handleCoordinateException(sIn);
      while(info != null){
        out.println(info);
        sIn = inputReader.readLine();
        info = handleCoordinateException(sIn);
      }
      c = new Coordinate(sIn);
    }
    return c;
  }

  /**
   * This method tries to handle the exception caused by the Placement
   * For version 2, adding the error handing for not URDL
   * @param input: the input string that user gives
   * @param v1OrV2: 1: v2, 0: v1
   * @return The error method
   */
  public String handlePlacementException(String input, int v1OrV2){
    String res = null;

    try{
      Placement p = new Placement(input);
      if(v1OrV2 == 0){
        if(p.getOrientation() != 'H' && p.getOrientation() != 'V'){
          res = "The input Placement is invalid, please try again for H or V!";
        }
      }
      //In Version 2
      if(v1OrV2 == 1){
        if(p.getOrientation() != 'U' && p.getOrientation() != 'R'
                && p.getOrientation() != 'D' && p.getOrientation() != 'L'){
          res = "The input Placement is invalid, please try again!";
        }
      }
    }
    catch(IllegalArgumentException e){
      res = "The input Placement is invalid, please try again!";
    }
    return res;
  }

  /**
   * This method reads the placement to place the new ship for the move action
   * Since we have already placed all the ships at the beginning
   * This method only works for the move action
   * @return
   */
  private Placement readPlacement(Coordinate c) throws IOException {
    int v1OrV2 = -1;
    //To get the type of the current ship
    Ship<Character> ship_moveFrom = battleShipBoard.getShip(c);
    String ship_moveFrom_name = ship_moveFrom.getName();
    if(ship_moveFrom_name == "Submarine" || ship_moveFrom_name == "Destroyer"){
      v1OrV2 = 0;
    }
    else{
      v1OrV2 = 1;
    }

    //Give a location(user input)
    out.println("Provide a place that you want to move the ship to(i.e. A0V, B0L):");
    String place = inputReader.readLine();
    String placeInfo = handlePlacementException(place, v1OrV2);
    while(placeInfo != null){
      out.println(placeInfo);
      place = inputReader.readLine();
      placeInfo = handlePlacementException(place, v1OrV2);
    }
    Placement placement = new Placement(place);
    return placement;
  }

  /**
   * This method reads the action from the input.
   * @return
   */
  private Action readActionFromPlayer(String choice) throws IOException {
    Action currAction = new Action();
    if(choice.toUpperCase(Locale.ROOT).equals("F")){
      out.println("Where do you want to fire at?");
      Coordinate sourceCoor = readCoordinate();
      currAction = new FireAction(sourceCoor);
    }
    else if(choice.toUpperCase(Locale.ROOT).equals("S")){
      out.println("Please enter the center of the Sonar Scan (i.e. A0)");
      Coordinate sourceCoor = readCoordinate();
      currAction = new SonarAction(sourceCoor);
      --sonarNum;
    }
    else if(choice.toUpperCase(Locale.ROOT).equals("M")){
      out.println("Please choose a ship that you want to move (type in a coordinate)");
      Coordinate sourceCoor = readMoveCoordinate(); //Move needs to check here has teh ship for self
//      out.println("Provide a place that you want to move the ship to(i.e. A0V, B0L):");
      Placement placement = readPlacement(sourceCoor);
      currAction = new MoveAction(sourceCoor, placement);
      --moveNum;
    }
    return currAction;
  }


  /**
   * This method checks whether the action is valid in multiple aspects
   * @return
   */
  private boolean isValidAction(Action action) throws IOException {
    if(action.getActionType().equals("F") || action.getActionType().equals("S")){
      return true; //Since the coordinate itself checks the boundary
    }
    else if(action.getActionType().equals("M")){
      Coordinate c = action.getSource();

      Ship<Character> ship_moveFrom = battleShipBoard.getShip(c);
      String ship_moveFrom_name = ship_moveFrom.getName();

      Placement p = action.getMoveTo();
      Ship<Character> s = shipCreationFns.get(ship_moveFrom_name).apply(p);
      String info = battleShipBoard.tryAddShip(s);
      //The error information is not null: the placement is invalid
      if(info != null) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method gets the choice of the player (Move, Fire, Sonar)
   * @return
   */
  private String getChoice() throws IOException {
    String sIn = inputReader.readLine();
    char curr = ' ';
    //Fix the null input: s.isEmpty()
    if(sIn.isEmpty() || sIn == "\n" || sIn == "" || sIn == null){
      curr = 'a'; //If it is the next line character, initialize it to another non-used letter
    }
    else{
      curr = sIn.charAt(0);
    }

    if(moveNum == 0){
      while(curr != 'F' && curr != 'S' &&
              curr != 'f' && curr != 's'){
        out.println("Invalid choice, your move has used up! please try again!");
        sIn = inputReader.readLine();
        curr = sIn.charAt(0);
      }
    }
    if(sonarNum == 0){
      while(curr != 'F' && curr != 'M'  &&
              curr != 'f' &&  curr != 'm'){
        out.println("Invalid choice, your sonar has used up! please try again!");
        sIn = inputReader.readLine();
        curr = sIn.charAt(0);
      }
    }
    //Case not sensitive!
    while(curr != 'F' && curr != 'M' && curr != 'S' &&
            curr != 'f' &&  curr != 'm' && curr != 's'){
      out.println("Invalid choice, please try again!");
      sIn = inputReader.readLine();
      curr = sIn.charAt(0);
    }
    String ans = "" + curr;
    return ans;
  }

  /**
   * This method sends the action to the server
   * @return String the choice the player choose
   */
  private String sendAction() throws IOException {
    out.println(getPrompt());
    String choice = getChoice();
    Action action = readActionFromPlayer(choice);
    isValidAction(action); //TODO: Add exceptions
    objectToServer.writeObject(action);
    return choice;
  }


  public int recvGameResult() throws IOException, ClassNotFoundException {
     int res = (Integer) objectFromServer.readObject();
     out.println("The current game result is: " + res);
     if(res == 2){
       out.println("Game continues");
     }
     return res;
  }


  public int runOneTurn() throws IOException, ClassNotFoundException {
    recvBoardTextView();
    String choice = sendAction();
    out.println("Waiting the other player to act...");

    //The fire gives the feedback whether it hits or missed
    //The sonar give the feedback of the scanned ships
    if(choice.toUpperCase(Locale.ROOT).equals("F") || choice.toUpperCase(Locale.ROOT).equals("S")){
      String turnInfo = (String) objectFromServer.readObject();
      out.println(turnInfo);
    }
    return recvGameResult();
  }



  public void playGame() throws IOException, ClassNotFoundException {
    int gameResult = -1;
    while (true) {
      gameResult = runOneTurn();
      if (gameResult == 0 || gameResult == 1) {
        System.out.println("Player " + gameResult + " is the winner!");
        System.out.println("Game ends!");
        return;
      }
    }
  }

  public TextPlayer getPlayer() {
    return player;
  }

  public static void main(String[] args) {
    int portNum = 11570;
    String hostname = "localhost";

    try {
      PlayerCommunicator client = new PlayerCommunicator(hostname, portNum);
      System.out.println(client + " connect to the Server successfully!");

      client.recvPlayerId();

      client.sendString("Hi, Player " + client.getPlayerId() + " has successfully connected with you!");
      out.println("finish sending string");

      //Client Place its ships
      client.doPlacementPhrase();
      client.sendString("Player " + client.getPlayerId() + " finished phrasing all ships!");
//      out.println("The current board is: \n" + client.getView().displayMyOwnBoard()); //Testing

      //Sending the boardView to the server
      client.sendBoardView();
      out.println("Waiting for the other player to finish phrasing...");

      //The game starts
      client.playGame();

      //Close all pipes
      client.closePipes();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
