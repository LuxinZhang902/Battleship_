package battleship.model;

import battleship.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;

import static java.lang.System.out;

public class BattleshipGame {


    private TextPlayer player;

    private final Socket socket;
    private final ObjectInputStream objectFromServer;
    private final ObjectOutputStream objectToServer;
    private BufferedReader inputReader;



    private BoardTextView view;

    private int playerId = -1;
    private BattleShipBoard battleShipBoard = null; //Receive the board from the server
    final ArrayList<String> shipsToPlace;

    private String currView;
    private int gameResult;



    final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns; //name, Map Function
        public final AbstractShipFactory<Character> shipFactory;

        int moveNum;
        int sonarNum;

        /**
         * Constructs the Client with the hostname and the port number
         * @throws IOException
         */
        public BattleshipGame() throws IOException {
            this.socket = new Socket("localhost", 11570);
            this.objectFromServer = new ObjectInputStream(socket.getInputStream());
            this.objectToServer = new ObjectOutputStream(socket.getOutputStream());
            this.inputReader = new BufferedReader(new InputStreamReader(System.in));
            this.battleShipBoard = new BattleShipBoard<Character>(10, 20, 'X');
            view = new BoardTextView(battleShipBoard);

            moveNum = 3;
            sonarNum = 3;
            shipCreationFns = new HashMap<>();
            shipFactory = new V2ShipFactory();
            setupShipCreationMap();

            shipsToPlace = new ArrayList<>();
            setupShipCreationList();
            currView = view.displayMyOwnBoard();
            gameResult = -1;
        }

        /**
         * This method receives the player id from the server
         * @throws IOException
         */
        public int recvPlayerId() throws IOException, ClassNotFoundException {
            Board<Character> b = new BattleShipBoard<Character>(10, 20, 'X');

            //Initialize the BufferedReader
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            //Initialize the factory
            V2ShipFactory factory = new V2ShipFactory(); //Version 2's factory
            playerId = (Integer) objectFromServer.readObject();
            System.out.println("received playerId = " + playerId + " successfully!");
            if(playerId == 0){
                player = new TextPlayer("A", b, input, out, factory);
            }
            else if(playerId == 1){
                player =  new TextPlayer("B", b, input, out, factory);
            }
            return playerId;
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
     * This method reads the input from the user's input and store it into Placement
     * @param prompt
     * @param v1Orv2 v1: 0, v2: 1
     * @return
     * @throws IOException
     */
    public Placement getPlacement(String prompt, int v1Orv2) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        handlePlacementException(s);
        Placement placement = new Placement(s);
        return placement;
    }
    /**
     *
     * @param shipName
     * @param v1OrV2 0: v1, 1: v2
     * @throws IOException
     */
    public void doOnePlacement(String shipName, int v1OrV2)
            throws IOException {
        //Continuing getting the information until it is correct
        //TODO if it is computer here,
        Placement p = getPlacement("Player " + player.getName() + " where do you want to place a " + shipName + "?", v1OrV2);
        Ship<Character> s = shipCreationFns.get(player.getName()).apply(p);
        String info = battleShipBoard.tryAddShip(s);
        while(info != null) {
            out.print(info);
            out.println();
            p = getPlacement("Player " + player.getName() + " where do you want to place a " + shipName + "?", v1OrV2);
            s = shipCreationFns.get(player.getName()).apply(p);
            info = battleShipBoard.tryAddShip(s);
        }
        out.print(view.displayMyOwnBoard());
    }
        public String getInitPrompt(){
            String prompt = "Player " + player.getName() + ": you are going to place the following ships: " +
                    "\n" +
                    "2 \"Submarines\"  (Pink) \n" +
                    "3 \"Destroyers\"   (Blue)\n" +
                    "3 \"Battleships\"  (Green)\n" +
                    "2 \"Carriers\"      (Yellow)\n";
            return prompt;
        }

        public String addShipToTheBoard(Placement placement, String name){
            Ship<Character> s = shipCreationFns.get(name).apply(placement);
            battleShipBoard.tryAddShip(s);
            view = new BoardTextView(battleShipBoard); //updating the board
//            out.println("current is: " + view.displayMyOwnBoard());
            return view.displayMyOwnBoard();
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

    /**
     * This method set(change) the number of each ship
     */
    protected void setupShipCreationList(){
        //TODO the proper number of times
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
//        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
//        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
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

        public String recvBoardTextView() throws IOException, ClassNotFoundException {
            currView = (String) objectFromServer.readObject();
            out.println(currView);
            return currView;
        }

        public String getCurrView(){
            return currView;
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
                throw new IllegalArgumentException("The input coordinate is invalid, please try again!");
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

         * @return The error method
         */
        public void handlePlacementException(String input){
            String res = null;

            try{
                Placement p = new Placement(input);

                if(p.getOrientation() != 'H' && p.getOrientation() != 'V' ||
                        p.getOrientation() != 'U' && p.getOrientation() != 'R'
                     && p.getOrientation() != 'D' && p.getOrientation() != 'L') {
                    res = "The input Placement is invalid, please try again for H or V!";
                }
            }
            catch(IllegalArgumentException e){
                 throw new IllegalArgumentException(res);
            }
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
            handlePlacementException(place);
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

        public void sendActionToServer(Action action) throws IOException {
            objectToServer.writeObject(action);
        }


        public int recvGameResult() throws IOException, ClassNotFoundException {
            gameResult = (Integer) objectFromServer.readObject();
            out.println("The current game result is: " + gameResult);
            if(gameResult == 2){
                out.println("Game continues");
            }
            return gameResult;
        }
        public String recvTurnResultStr() throws IOException, ClassNotFoundException {
            String turnInfo = (String) objectFromServer.readObject();
            out.println(turnInfo);
            return turnInfo;
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

    public ArrayList<String> getShipsToPlace() {
        return player.getShipsToPlace();
    }

        public static void main(String[] args) {
            int portNum = 11570;
            String hostname = "localhost";

            try {
                battleship.Client client = new battleship.Client(hostname, portNum);
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

    public HashMap<String, Function<Placement, Ship<Character>>> getShipCreationFns() {
        return shipCreationFns;
    }

    public BoardTextView getView() {
        return view;
    }

    public int getGameResult(){
            return gameResult;
    }
    public String disPlayCurrBoard(){
            BoardTextView view = new BoardTextView(battleShipBoard);
            return view.displayMyOwnBoard();
    }



}
