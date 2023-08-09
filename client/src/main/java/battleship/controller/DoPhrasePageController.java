package battleship.controller;

import battleship.Placement;
import battleship.Ship;
import battleship.ShowViews;
import battleship.model.BattleshipGame;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static java.lang.System.err;
import static java.lang.System.out;

public class DoPhrasePageController {
    private int playerId;

    private Stage stage;
    private BattleshipGame gameEntity;

    @FXML
    private Label playerInfo;


    @FXML
    private Button placeButton;
    @FXML
    private TextField userText;

    @FXML
    private AnchorPane boardTextView;


//    @FXML
//    private Text displayBoardView;

    @FXML
    private AnchorPane resultPane;

    @FXML
    private Label promptStr;

    @FXML
    private Label placeShipPrompt;
    String currText;
    ArrayList<String> shipsToPlace;
    int shipIdx;
    @FXML
    private Button finishButton;
    @FXML
    private Label waitInfo;

    GridGenerator gridPaneGenerator;
    @FXML
    GridPane gridPane;

    public DoPhrasePageController(int id, Stage _stage, BattleshipGame _gameEntity) {
        this.playerId = id;
        this.stage = _stage;
        this.gameEntity = _gameEntity;
        playerInfo = new Label();
        shipsToPlace = new ArrayList<>();
        shipIdx = 0;
        placeShipPrompt = new Label();
    }

    @FXML
    void onFinishButton(MouseEvent event) throws IOException {
        waitInfo.setVisible(true);
        Thread th = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                gameEntity.sendString("Player " + playerId + " finished phrasing all ships!");
                gameEntity.sendBoardView();

                out.println("The currView received from server is: " + gameEntity.recvBoardTextView());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ShowViews.showGameView(stage, "/ui/mainPage.fxml", gameEntity);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                return null;
            }

        });
        th.setDaemon(true);
        th.start();


    }

    @FXML
    void placeTheShip(MouseEvent event){
        userText.clear();
        String currBoard = "";
        shipsToPlace = gameEntity.getShipsToPlace();
        String name = shipsToPlace.get(shipIdx);
        //Placing the ship
        Placement placement = readPlacement(); //Not the moved ship, use 0
        currBoard = gameEntity.addShipToTheBoard(placement, name);
        if(shipIdx == shipsToPlace.size()){
            throw new IllegalArgumentException("All Ships Have Been Placed! Continue by Press Finish Button!");
        }
        ++ shipIdx;
        out.println(currBoard);
//        displayBoardView.setText(currBoard);
        strToGrid(currBoard);
    }

    /**
     * This method reads the input from the user's input and store it into Placement
     * @return
     * @throws IOException
     */
    public Placement readPlacement(){
        gameEntity.handlePlacementException(currText);
        Placement placement = new Placement(currText);
        return placement;
    }

    @FXML
    public void getUserInput(ActionEvent event) {
        currText = userText.getText();
        // Do something with the selected value
        System.out.println("You will place the ship on " + currText);
    }


    public ArrayList<Character> eliminateSpace(String s){
        ArrayList<Character> ans = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == 'X'){
                ans.add(s.charAt(i));
            }
            else if(s.charAt(i) != '|' && !Character.isUpperCase(s.charAt(i)) && i != 1 && i != s.length() - 2){
                ans.add(s.charAt(i));
            }
        }
        return ans;
    }
    public void strToGrid(String currView){
        String[] oriLines = currView.split("\n");
        ArrayList<ArrayList<Character>> eachLine = new ArrayList<>();
        for(int i = 0; i < oriLines.length; i++){
            if(i != 0 && i != oriLines.length - 1){
                ArrayList<Character> lineNoSplit = eliminateSpace(oriLines[i]);
                eachLine.add(lineNoSplit);
            }
        }
        for(ArrayList s: eachLine){
            for(Object c: s){
                System.out.print(c);
            }
            System.out.println();
        }
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Creating header row
        Label headerLabel = new Label("");
        GridPane.setColumnSpan(headerLabel, 10);
        GridPane.setRowIndex(headerLabel, 0);
        GridPane.setColumnIndex(headerLabel, 0);
        gridPane.getChildren().add(headerLabel);

        // Creating column headers
        String[] headers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < headers.length; i++) {
            Label header = new Label();
            header.setAlignment(Pos.CENTER);
            GridPane.setRowIndex(header, 1);
            GridPane.setColumnIndex(header, i+1);
            gridPane.getChildren().add(header);
        }

        // Creating rows and columns for board
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
        for (int i = 0; i < eachLine.size(); i++) {
            Label rowLabel = new Label();
            rowLabel.setAlignment(Pos.CENTER_RIGHT);
            GridPane.setRowIndex(rowLabel, i+2);
            GridPane.setColumnIndex(rowLabel, 0);
            gridPane.getChildren().add(rowLabel);

            for (int j = 0; j < eachLine.get(i).size(); j++) {
                Rectangle square = new Rectangle(15, 15);
//                Label squareLabel = new Label(" ");
//                square.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
                GridPane.setRowIndex(square, i+2);
                GridPane.setColumnIndex(square, j+1);
                gridPane.getChildren().add(square);

                if (eachLine.get(i).get(j) == 's') {
                    square.setFill(Color.PINK);
                }
                else if (eachLine.get(i).get(j) == 'd') {
                    square.setFill(Color.BLUE);
                }
                else if (eachLine.get(i).get(j) == 'b') {
                    square.setFill(Color.GREEN);
                }
                else if (eachLine.get(i).get(j) == 'c') {
                    square.setFill(Color.YELLOW);
                }
                else if (eachLine.get(i).get(j) == '*'){
                     square.setFill(Color.WHITE);
                }
            }
        }
    }


    /**
     * initialize the ChooseActionController
     * show the text of label playerColor
     */
    public void initialize(){
        strToGrid(gameEntity.disPlayCurrBoard());
        promptStr.setText(gameEntity.getInitPrompt());
        placeShipPrompt.setText("Where do you want to place your ship?");
        if (playerId == 0) {
            playerInfo.setText("Start Your Placements By Typing the Place\nPress Enter when you finish!");
        } else {
            playerInfo.setText("Start Your Placements By Typing the Place\nPress Enter when you finish!");
        }
    }
}
