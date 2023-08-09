package battleship;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static java.lang.System.in;
import static java.lang.System.out;

public class Server {
    private final ServerSocket serverSock;
    private final ArrayList<Socket> clientSockets;
    private final ArrayList<ObjectOutputStream> objectsToClients;
    private final ArrayList<ObjectInputStream> objectsFromClients;

    private HashMap<Integer, BoardTextView> playerBoardView; //The player's id and its boardView
    private HashMap<Integer, Board<Character>> playerBoard; //The player's id and its board

    private ArrayList<TextPlayer> allPlayers;

    private  HashMap<Integer, Action> playerAction; //The player's id and its action in one turn

    final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns; //name, Map Function

    public final AbstractShipFactory<Character> shipFactory;


    /**
     * Constructs Server with port number
     * @param _portNum
     * @throws Exception
     */
    public Server(int _portNum) throws Exception {
        this.serverSock = new ServerSocket(_portNum);
        this.clientSockets = new ArrayList<>();
        this.objectsToClients = new ArrayList<>();
        this.objectsFromClients = new ArrayList<>();
        playerBoardView = new HashMap<>();
        // Get all players
        Board<Character> b = new BattleShipBoard<Character>(10, 20, 'X');
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        V2ShipFactory factory = new V2ShipFactory(); //Version 2's factory
//        TextPlayer p1 = new TextPlayer("A", b, input, out, factory);
//        TextPlayer p2 = new TextPlayer("B", b, input, out, factory);
        allPlayers = new ArrayList<>();
        shipCreationFns = new HashMap<>();
        playerBoard = new HashMap<>();
        playerAction = new HashMap<>();
        shipFactory = new V2ShipFactory();
        setupShipCreationMap();
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
     * This method connects the client using ObjectOutputStream and ObjectInputStream
     * @throws IOException
     */
    public void connectClients() throws IOException {
        this.clientSockets.add(serverSock.accept());
        objectsToClients.add(new ObjectOutputStream(clientSockets.get(0).getOutputStream()));
        objectsFromClients.add(new ObjectInputStream(clientSockets.get(0).getInputStream()));
        System.out.println("Player 0 connects to Server successfully!");

        this.clientSockets.add(serverSock.accept());
        objectsToClients.add(new ObjectOutputStream(clientSockets.get(1).getOutputStream()));
        objectsFromClients.add(new ObjectInputStream(clientSockets.get(1).getInputStream()));
        System.out.println("Player 1 connects to Server successfully!");
    }

    /**
     * This method closes all pipes
     * @throws IOException
     */
    public void closePipes() throws IOException {
        objectsToClients.get(0).close();
        objectsToClients.get(1).close();
        objectsFromClients.get(0).close();
        objectsFromClients.get(1).close();
        clientSockets.get(0).close();
        clientSockets.get(1).close();
        serverSock.close();
    }

    public void recvBoardView() throws IOException, ClassNotFoundException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        V2ShipFactory factory = new V2ShipFactory(); //Version 2's factory
        for(int i = 0; i < 2; i++) {
            //Receive the done info
            String str = (String) objectsFromClients.get(i).readObject();
            out.println(str);

            BoardTextView btv = (BoardTextView) objectsFromClients.get(i).readObject();
            playerBoardView.put(i, btv);
            out.println(btv.displayMyOwnBoard());

            //Putting into board
            BattleShipBoard currBoard =(BattleShipBoard) btv.getBattleshipBoard();
            playerBoard.put(i, currBoard);

            if(i == 0){
                TextPlayer p1 = new TextPlayer("A", currBoard, input, out, factory);
                allPlayers.add(p1);
            }
            if(i == 1){
                TextPlayer p2 = new TextPlayer("B", currBoard, input, out, factory);
                allPlayers.add(p2);
            }
        }

        out.println("Successfully receive the boardTextView from both players");
    }


    public void sendPlayerID() throws IOException {
        objectsToClients.get(0).writeObject(0);
        objectsToClients.get(1).writeObject(1);
        System.out.println("assign and send playerId to all Players!");
//        objectsToClients.get(0).reset();
//        objectsToClients.get(1).reset();
    }

    public void sendGameResult(int endResult) throws IOException {
        objectsToClients.get(0).writeObject(endResult);
        objectsToClients.get(1).writeObject(endResult);
        System.out.println("Sending the game result to all players!");
//        objectsToClients.get(0).reset();
//        objectsToClients.get(1).reset();
    }

    public void sendBoardView() throws IOException {
        objectsToClients.get(0).writeObject(playerBoardView.get(0));
        objectsToClients.get(1).writeObject(playerBoardView.get(1));
        System.out.println("assign and send boardTextView to all Players!");
    }

    public void recvStr() throws IOException, ClassNotFoundException {
        String str1 = (String) objectsFromClients.get(0).readObject();
        out.println(str1);
        String str2 = (String) objectsFromClients.get(1).readObject();
        out.println(str2);
//        out.println("Successfully send the board to the server");
    }

    /**
     * This method is to test the correctness of the boardView
     */
    public void displayCurrStage(){
        out.println("Player A's view: \n");
        out.println(playerBoardView.get(0).displayMyBoardWithEnemyNextToIt(playerBoardView.get(1),
                "Your Ocean", "Enemy's Ocean"));

        out.println("Player B's view: \n");
        out.println(playerBoardView.get(1).displayMyBoardWithEnemyNextToIt(playerBoardView.get(0),
                "Your Ocean", "Enemy's Ocean"));
    }

    public void sendBoardViewStr(String str, int id) throws IOException {
        objectsToClients.get(id).writeObject(str);
        objectsToClients.get(id).reset();

    }

    /**
     * The server only sends the player the string to display
     * @throws IOException
     */
    public void sendBoardTextViewForAll() throws IOException {
        out.println("Sending the BoardTextView to players");
        //Sending the board
        String displayAB = playerBoardView.get(0).displayMyBoardWithEnemyNextToIt(playerBoardView.get(1), "A's Ocean",
                "B's Ocean");
        sendBoardViewStr(displayAB, 0);
        String displayBA = playerBoardView.get(1).displayMyBoardWithEnemyNextToIt(playerBoardView.get(0), "B's Ocean",
                "A's Ocean");
        sendBoardViewStr(displayBA, 1);

    }

    /**
     * This method receives the actions from all players (only one action each turn)
     */
    public void recvAction() throws IOException, ClassNotFoundException {
        Action action_p1 = (Action) objectsFromClients.get(0).readObject();
        out.println("Received action from p1: " + action_p1.getActionType() + "Coordinate: " + action_p1.getMoveTo());
        playerAction.put(0, action_p1);

        Action action_p2 = (Action) objectsFromClients.get(1).readObject();
        out.println("Received action from p2: " + action_p1.getActionType() + "Coordinate: " + action_p1.getMoveTo());
        playerAction.put(1, action_p2);
    }


    /**
     * This method moves the ship
     * @param playerID
     * @param move
     * @throws IOException
     */
    public void doMove(int playerID, Action move) throws IOException {
        Coordinate chooseFrom = move.getSource();
        Placement toPlace = move.getMoveTo();
        move.reduceActionNum();
        int resource = move.getRemainActions();
        out.println("The current remaining move resource is: " + resource);

        //Record this ship's info
        Ship ship_moveFrom = playerBoard.get(playerID).getShip(chooseFrom);
        String ship_moveFrom_name = ship_moveFrom.getName();

        //Remove the original place's ship
        playerBoard.get(playerID).removeShip(ship_moveFrom);


        //Put the ship(copy of this ship's name) in the new location
        Ship<Character> ship_moveTo = shipCreationFns.get(ship_moveFrom_name).apply(toPlace);
        playerBoard.get(playerID).tryAddShip(ship_moveTo);

        //remove the ship
        ship_moveTo.moveShip(ship_moveFrom, ship_moveTo);
        String str = "You move the ship from " + move.getSource().toString() + ", and place it to " + move.getMoveTo().toString();
        objectsToClients.get(playerID).writeObject(str);

    }

    /**
     * This method does fire to the enemy's board
     * //TODO: send a string and a board
     */
    public void doFire(int playerID, Action action) throws IOException {
        Board<Character> enemyBoard = playerBoard.get(1 - playerID);
        Coordinate fireAt = action.getSource(); //The coordinate to fire at

        //hit the enemy
        enemyBoard.fireAt(fireAt); //fire at enemy board
        String str = "";

        if (enemyBoard.whatIsAtForSelf(fireAt) == null) {
            str = "You missed!";
        } else if (enemyBoard.whatIsAtForEnemy(fireAt) == 's') {
            str = "You hit a submarine!";
        } else if (enemyBoard.whatIsAtForEnemy(fireAt) == 'd') {
           str = "You hit a destroyer!";
        } else if (enemyBoard.whatIsAtForEnemy(fireAt) == 'b') {
            str = "You hit a battleship!";
        } else {
            str = "You hit a carrier!";
        }
        objectsToClients.get(playerID).writeObject(str);

    }

    /**
     * This method prints the count information
     * @param count
     */
    public String getCountInfo(HashMap<String, Integer> count){
        String info = "Submarines occupy " +  count.get("Submarine")+ " squares\n" +
                "Destroyers occupy " +  count.get("Destroyer")+ " squares\n" +
                "Battleships occupy " +  count.get("Battleship")+ " squares\n" +
                "Carriers occupy " + count.get("Carrier") + " square\n";
        out.println(info);
        return info;
    }

    /**
     * this method does the Sonar action
     */
    public void doSonar(int playerID, Action action) throws IOException {
        out.println("Please enter the center of the Sonar Scan (i.e. A0)");
        Coordinate coordinate = action.getSource();

        //Adding the scanning region to the enemy board
        Board<Character> enemyBoard = playerBoard.get(1 - playerID);
        ArrayList<Coordinate> scanned = enemyBoard.addScanRegion(coordinate);

        //Getting the ship name with its corresponding count
        HashMap<String, Integer> count = enemyBoard.getEachShipSonaredNum(coordinate, scanned);

        //Printing the count info
        objectsToClients.get(playerID).writeObject(getCountInfo(count));

    }

    /**
     * This method gets the turn result for each turn
     */
    private int getTurnResult() {
        int res = -1;
        if(allPlayers.get(0).loseGame()){
            res = 1;
        }
        else if(allPlayers.get(1).loseGame()){
            res = 0;
        }
        else if(!allPlayers.get(0).loseGame() && !allPlayers.get(1).loseGame()){
            res = 2;
        }
        return res;
    }

    /**
     * This method executes the action for both player in order
     */
    public void executeAction() throws IOException {
        for(int i = 0; i < 2; i++){
            Action currAction = playerAction.get(i);
            if(currAction.actionType.equals("F") || currAction.getRemainActions() == 0){
                doFire(i, currAction);
            }
            else if(currAction.getActionType().equals("M")){
                doMove(i, currAction);
            }
            else if(currAction.actionType.equals("S")){
                doSonar(i, currAction);
            }
        }
    }



    public int runOneTurn() throws IOException, ClassNotFoundException {
        sendBoardTextViewForAll();
        recvAction();
        executeAction(); //Sending one string
        int result = getTurnResult();
        out.println("Current turn result is: " + result);
        sendGameResult(result);
        if(result == 1 || result == 0){
            sendBoardTextViewForAll();
        }
        return result;
    }



    /**
     * This method Plays the game until the game ends
     */
    public void playGame() throws IOException, ClassNotFoundException {
        int result = -1; //initialize the end flag
        while(true){
            if(result == 0){
                out.println( "Player " + "A" + " WINS!\n");
                break;
            }
            else if(result == 1){
                out.println( "Player " + "B" + " WINS!\n");
                break;
            }
            else{
                result = runOneTurn();
            }
        }
    }


    public static void main(String[] args) {
        int portNum = 11570;
        try {
            Server server = new Server(portNum);
            System.out.println("Create Server successfully!");
            server.connectClients();
            System.out.println("Both clients connect to the Server successfully!\n");

            server.sendPlayerID();

            //Receive the playerID information
            server.recvStr();

            //Receive the Board
            server.recvBoardView();
            out.println("Finish receiving board");
           server.displayCurrStage();

           //The game starts
            server.playGame();

            server.closePipes();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
