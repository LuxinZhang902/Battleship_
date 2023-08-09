package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class TextPlayer {

    //Shared fields
    private final Board<Character> theBoard;
//    private final Board<Character> computerBoard;

    private final BoardTextView view;
    public final BufferedReader inputReader;

    public final PrintStream out;
    public final AbstractShipFactory<Character> shipFactory;
    private final String name;



    final ArrayList<String> shipsToPlace;
    final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns; //name, Map Function

    int actions; //3 in total
    int moves;
    int sonars;
    int computerHit;




    private int isComputer; //1: computer, 0: human
    public void setIsComputer(int isComputer) {
        this.isComputer = isComputer;
    }

    /**
     * The getter function for p1 to see whether it is computer
     * @return
     */
    public int isComputer() {
        return isComputer;
    }

    public Board<Character> getTheBoard() {
        return theBoard;
    }

    public String getName() {
        return name;
    }

    public BoardTextView getView() {
        return view;
    }


    /**
     * This method gets the status for both players. It recurrently prompts the user to give input until it is valid
     * @return int 1: computer, 0: human
     * @throws IOException
     */
    public int getStatus() throws IOException {
        out.println("Is Player " + this.name + " Computer? (y: computer, n: human): ");
        String s = inputReader.readLine();
        char input = s.charAt(0);
        while(input != 'y' && input != 'n' && input != 'Y' && input != 'N'){
            out.println("Invalid Input! Give 'y' if this player is computer, 'n' if this player is human!");
            s = inputReader.readLine();
            input = s.charAt(0);
        }
        //If it is computer, return 1
        if(input == 'y' || input == 'Y'){
            out.println("Player " + name + " is a Computer!");
            return 1;
        }
        else{
            out.println("Player " + name + " is a Human!");
            return 0;
        }
    }

    /**
     * This constructor is generated followed the README's structure
     * TextPlayer p1 = new TextPlayer("A", b1, input, System.out, factory);
     * @param theBoard
     * @param inputReader
     * @param out
     * @param shipFactory
     */
    public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out,
                      AbstractShipFactory<Character> shipFactory) {
        this.name = name;

//        if(isComputer == 1){
//            this.computerBoard = theBoard;
//        }
//        else{
//            this.theBoard = theBoard;
//        }
        this.theBoard = theBoard;


        this.inputReader = inputReader;
        this.out = out;
        this.shipFactory = shipFactory;
        this.view = new BoardTextView(theBoard);
        //Setting up the map
        this.shipCreationFns = new HashMap<>();
        setupShipCreationMap();

        //Setting up the list
        this.shipsToPlace = new ArrayList<>();
        setupShipCreationList();

        this.actions = 3;
        //TODO TO MAKE SURE
        this.sonars = 3;
        this.moves = 3;

        this.computerHit = 0;
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
    }

    /**
     * This method reads the input from the user's input and store it into Placement
     * @param prompt
     * @param v1Orv2 v1: 0, v2: 1
     * @return
     * @throws IOException
     */
    public Placement readPlacement(String prompt, int v1Orv2) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
//        while(s == null){
//            out.println("The input string is null. Try again!");
//            s = inputReader.readLine();
////            assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
////            throw new IllegalArgumentException("The input string is null.");
//        }

        String info = handlePlacementException(s, v1Orv2);
        while(info != null){
            out.println(info);
            s = inputReader.readLine();
            info = handlePlacementException(s, v1Orv2);
        }
        Placement placement = new Placement(s);
        return placement;
    }
    public void getOutputMessage(String output) throws IOException{
        out.println(output);
    }

    /**
     *
     * @param shipName
     * @param createFn
     * @param v1OrV2 0: v1, 1: v2
     * @throws IOException
     */
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn, int v1OrV2)
            throws IOException {
        //Continuing getting the information until it is correct
        //TODO if it is computer here,
        Placement p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?", v1OrV2);
        Ship<Character> s = createFn.apply(p);
        String info = theBoard.tryAddShip(s);
        while(info != null) {
            out.print(info);
            out.println();
            p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?", v1OrV2);
            s = createFn.apply(p);
            info = theBoard.tryAddShip(s);
        }
        out.print(view.displayMyOwnBoard());
    }

    /**
     *
     * @param createFn
     * @param v1OrV2 0: v1, 1: v2
     * @throws IOException
     */
    public Ship<Character> doNewPlacement(Function<Placement, Ship<Character>> createFn, int v1OrV2, Placement p)
            throws IOException {
        //Continuing getting the information until it is correct
        Ship<Character> s = createFn.apply(p);
        String info = theBoard.tryAddShip(s);
        while(info != null) {
            out.print(info);
            out.println();
            p = readPlacement("Provide a place that you want to move the ship to(i.e. A0V, B0L):", v1OrV2);
            s = createFn.apply(p);
            info = theBoard.tryAddShip(s);
        }
        out.print(view.displayMyOwnBoard());
        return s;
    }

    /**
     * This method gets the generated array for the places of putting ships without error
     */
    public ArrayList<Placement> getGenerateArrayForShips(){
        ArrayList<Placement> ans = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        input.add("a0v");
        input.add("a1v");
        input.add("c2h");
        input.add("b3h");
        input.add("d7h");
        input.add("f2u");
        input.add("i2u");
        input.add("k5l");
        input.add("n0r");
        input.add("q4l");
        for(int i = 0; i < input.size(); i++){
            if(i < 5){
                handlePlacementException(input.get(i), 0);
                ans.add(new Placement(input.get(i)));
            }
            else{
                handlePlacementException(input.get(i), 1);
                ans.add(new Placement(input.get(i)));
            }
        }
        return ans;
    }

    public void doPlacementPhase() throws IOException{
        //If the player is human
        if(this.isComputer == 0) {
            //Display the empty board
            //TODO: Check whether to use out.println or System.out.println
            out.print(view.displayEmptyBoard());
            String prompt = "Player " + this.name + ": you are going to place the following ships (which are all\n" +
                    "rectangular). For each ship, type the coordinate of the upper left\n" +
                    "side of the ship, followed by either H (for horizontal) or V (for\n" +
                    "vertical).  For example M4H would place a ship horizontally starting\n" +
                    "at M4 and going to the right.  You have\n" +
                    "\n" +
                    "2 \"Submarines\" ships that are 1x2 \n" +
                    "3 \"Destroyers\" that are 1x3\n" +
                    "3 \"Battleships\" that are 1x4\n" +
                    "2 \"Carriers\" that are 1x6\n";
            //Printing the instructions message
            out.print(prompt);
            int v1OrV2;
            //iterator the shipsToPlace
            for (int i = 0; i < shipsToPlace.size(); i++) {
                String name = shipsToPlace.get(i);
                //Placing the ship
                //If it is submarine or destroyer, get the version1's doPlacement
                if (name == "Submarine" || name == "Destroyer") {
                    v1OrV2 = 0;
                } else {
                    v1OrV2 = 1;
                }
                doOnePlacement(name, shipCreationFns.get(name), v1OrV2); //Not the moved ship, use 0
            }
//            out.print(view.displayMyOwnBoard()); //One more time after the last ship is placed
        }
        //else, the player is computer
        else{
            for (int i = 0; i < shipsToPlace.size(); i++) {
                String name = shipsToPlace.get(i);
                ArrayList<Placement> arr = getGenerateArrayForShips();
                Ship<Character> s = shipCreationFns.get(name).apply(arr.get(i));
                theBoard.tryAddShip(s); //NO Problem!
            }
        }
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
     * This method gets the choice of the player. If it is invalid, continue getting it
     * @return A strign contains of the input choice
     * @throws IOException
     */
    public char getChoice() throws IOException {
        String sIn = inputReader.readLine();
        char curr = ' ';
        //Fix the null input: s.isEmpty()
        if(sIn.isEmpty() || sIn == "\n" || sIn == "" || sIn == null){
            curr = 'a'; //If it is the next line character, initialize it to another non-used letter
        }
        else{
            curr = sIn.charAt(0);
        }

//        out.println(curr); // For Testing
        if(moves == 0){
            while(curr != 'F' && curr != 'S' &&
                    curr != 'f' && curr != 's'){
                out.println("Invalid choice, your move has used up! please try again!");
                sIn = inputReader.readLine();
                curr = sIn.charAt(0);
            }
        }
        if(sonars == 0){
            while(curr != 'F' && curr != 'M'  &&
                    curr != 'f' &&  curr != 'm'){
                out.println("Invalid choice, your sonar has used up! please try again!");
                sIn = inputReader.readLine();
//                if(sIn.isEmpty() || sIn == "\n" || sIn == "" || sIn == null){
//                    curr = 'a'; //If it is the next line character, initialize it to another non-used letter
//                }
//                else{
//                    curr = sIn.charAt(0);
//                }
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
        return curr;
    }
    public String getPrompt(){
        return " Possible actions for Player " + this.name + ":\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (" + moves + " remaining)\n" +
                " S Sonar scan (" + sonars + " remaining)\n" +
                "\n" +
                "Player " + this.name + ", what would you like to do?";
    }

    /**
     * This method is called when the user chooses move mode
     * It moves a ship to another square
     */
    public void doMove() throws IOException {
        //Select a ship
        out.println("Please choose a ship that you want to move (type in a coordinate)");
        String sIn = inputReader.readLine();


        //It purely checks whether the input coordinate is correct
        String info = handleCoordinateException(sIn);
        while(info != null){
            out.println(info);
            sIn = inputReader.readLine();
            info = handleCoordinateException(sIn); 
       }
        Coordinate c = new Coordinate(sIn);

        //Checking whether there is a ship in this coordinate
        //If there is no ship, re-prompt again
        while(theBoard.whatIsAtForSelf(c) == null){
            out.println("There is no ship at " + c.toString() + ", please choose another Coordinate!");
            sIn = inputReader.readLine();
            //It purely checks whether the input coordinate is correct
            info = handleCoordinateException(sIn);
            while(info != null){
                out.println(info);
                sIn = inputReader.readLine();
                info = handleCoordinateException(sIn);
            }
            c = new Coordinate(sIn);
        }

        //Record this ship's info
        //Coordinate c must contain a ship, this requirement is checked before. the ship_moveFrom will never be null!
        Ship<Character> ship_moveFrom = theBoard.getShip(c);
        String ship_moveFrom_name = ship_moveFrom.getName();
//        Placement place_from = ship_moveFrom.get
        int v1OrV2;
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


        theBoard.removeShip(ship_moveFrom);


        //Put the ship(copy of this ship's name) in the new location
        //v1Orv2: 1
        Ship<Character> ship_moveTo = doNewPlacement(shipCreationFns.get(ship_moveFrom.getName()), v1OrV2, placement);



        //remove the ship
        ship_moveTo.moveShip(ship_moveFrom, ship_moveTo);

        //display the current info
        out.print(view.displayMyOwnBoard());

    }

    public void getCountInfo(HashMap<String, Integer> count){
        String info = "Submarines occupy " +  count.get("Submarine")+ " squares\n" +
                "Destroyers occupy " +  count.get("Destroyer")+ " squares\n" +
                "Battleships occupy " +  count.get("Battleship")+ " squares\n" +
                "Carriers occupy " + count.get("Carrier") + " square";
        out.println(info);
    }

    /**
     * This method do the sonar scan. It prompts for the center coordinates of the sonar scan.
     * Any coordinate on the game board is **valid**, even if part of the scan will go off the edges of the board
     */
    public void doSonar(Board<Character> enemyBoard) throws IOException {
        //Getting the center of the sonar scan (read until it is valid)
        out.println("Please enter the center of the Sonar Scan (i.e. A0)");
        String sIn = inputReader.readLine();

        //It checks whether it is inside the board
        String info = handleCoordinateException(sIn);
        while(info != null){
            out.println(info);
            sIn = inputReader.readLine();
            info = handleCoordinateException(sIn);
        }
        Coordinate c = new Coordinate(sIn);

        //Adding the scanning region to the enemy board
        ArrayList<Coordinate> scanned = enemyBoard.addScanRegion(c);
//        enemyBoard.addScanRegion(c);
        //Getting the ship name with its corresponding count
        HashMap<String, Integer> count = enemyBoard.getEachShipSonaredNum(c, scanned);

        //Printing the count info
        getCountInfo(count);
    }
    public ArrayList<Coordinate> getAllCoordinateOnBoard(){
        ArrayList<Coordinate> ans = new ArrayList<>();
        //Go through each col and then each row
        for(int i = 0; i < theBoard.getHeight(); i++){
            for(int j = 0; j < theBoard.getWidth(); j++){
                ans.add(new Coordinate(i, j)); //Since all coordinates are on the board, we do not need to check!
            }
        }
        return ans;
    }
    /**
     * This method plays one turn. The prompt information is eliminated once all actions are used up.
     * If all 3 actions are used up, directly come to version 1
     * @param enemyName since it can only get access self name, pass in the enemyName
     * @param enemyBoard
     * @param enemyView
     */
    public void playOneTurn(String enemyName, Board<Character> enemyBoard, BoardTextView enemyView)
            throws IOException {
        String h1 = "Your Ocean";
        String h2 = "Enemy " + enemyName + "'s Ocean";


        //If it is the computer, hit the enemy and only print the outcome of the action
        //Here in this version, there is no special movement for computer
        //TODO make the computer smarter
        if(isComputer == 1){
            out.println("It is Computer's Turn!");
            ArrayList<Coordinate> coors = getAllCoordinateOnBoard();
//            if(computerHit == 200){
//                computerHit = 0;
//            }
            Coordinate c = coors.get(computerHit);
            enemyBoard.fireAt(c); //fire at enemy board
            theBoard.whatIsAtForSelf(c);
            enemyBoard.fireAt(coors.get(computerHit)); //fire at enemy board
//            System.out.println("current enemy at c: " + enemyBoard.whatIsAtForEnemy(c));
            if(enemyBoard.whatIsAtForSelf(c) == null){
                out.println("You missed!");
            }
            else if(enemyBoard.whatIsAtForEnemy(c) == 's'){
                out.println("You hit a submarine at " + c.getDirectString() + "!");
            }
            else if(enemyBoard.whatIsAtForEnemy(c) == 'd'){
                out.println("You hit a destroyer at " + c.getDirectString() + "!");
            }
            else if(enemyBoard.whatIsAtForEnemy(c) == 'b'){
                out.println("You hit a battleship at " + c.getDirectString() + "!");
            }
//            else if(enemyBoard.whatIsAtForEnemy(c)== 'c'){
//                out.println("You hit a carrier at " + c.getDirectString() + "!");
//            }
            else{
                out.println("You hit a carrier at " + c.getDirectString() + "!"); 
           }
            computerHit ++; //Updating the index of computerHit to let computer go through to hit the board
//            out.println(c.toString());
//            out.println(computerHit);
        }
        //If it is human
        else {
            //Show the board
            out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, h1, h2));

            String turnInfo = "Player " + this.name + "'s turn\n";
            out.println(turnInfo);

            char choice = ' ';
            //Decide whether used up all the actions
            if ((moves != 0 || sonars != 0)) {
                String prompt = getPrompt();
                out.println(prompt);
                choice = getChoice();
            }

            //Do the action
            if ((moves == 0 && sonars == 0) || choice == 'f' || choice == 'F') {
                //Prompt Sentence
                String fireAtPrompt = "Where do you want to fire at?";
                out.println(fireAtPrompt);

                //hit the enemy
                String sIn = inputReader.readLine();
                String info = handleCoordinateException(sIn);
                while (info != null) {
                    out.println(info);
                    sIn = inputReader.readLine();
                    info = handleCoordinateException(sIn);
                }
                Coordinate c = new Coordinate(sIn);
                Character shipShape = theBoard.whatIsAtForSelf(c);
                enemyBoard.fireAt(c); //fire at enemy board
//            System.out.println("current enemy at c: " + enemyBoard.whatIsAtForEnemy(c));
                if (enemyBoard.whatIsAtForSelf(c) == null) {
                    out.println("You missed!");
                } else if (enemyBoard.whatIsAtForEnemy(c) == 's') {
                    out.println("You hit a submarine!");
                } else if (enemyBoard.whatIsAtForEnemy(c) == 'd') {
                    out.println("You hit a destroyer!");
                } else if (enemyBoard.whatIsAtForEnemy(c) == 'b') {
                    out.println("You hit a battleship!");
                } else {
                    out.println("You hit a carrier!");
                }
            }
            if ((choice == 'M' || choice == 'm') && moves != 0) {
                --moves;
//            -- actions;
                doMove();
            }
            if ((choice == 'S' || choice == 's') && sonars != 0) {
                --sonars;
//            -- actions;
                doSonar(enemyBoard);
            }

            //Show the board again
            out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, h1, h2));
        }
    }
    public ArrayList<String> getShipsToPlace() {
        return shipsToPlace;
    }


    /**
     *
     * @return true if lose the game, false if it has not lost the game
     */
    public boolean loseGame(){
        return theBoard.isAllShipsSunk();
    }
}
