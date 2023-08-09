package battleship.controller;

import battleship.*;
import battleship.model.BattleshipGame;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class TurnResultPageController {
    int playerId;
    Stage stage;
    BattleshipGame gameEntity;

    @FXML
    Label playerInfo;
    @FXML
    private Text displayBoard;

    @FXML
    private Label waitInfo;


    @FXML
    private Button nextButton;

    @FXML
    private AnchorPane boardTextView;

    @FXML
    private AnchorPane resultPane;

    @FXML
    private Label turnResult;

    String currText;
    private int gameResult;
    String currResultStr;
    @FXML
    private GridPane gridPane;



    public TurnResultPageController(int id, Stage _stage, BattleshipGame _gameEntity) {
        this.playerId = id;
        this.stage = _stage;
        this.gameEntity = _gameEntity;
        playerInfo = new Label();
        currText = "";
        currResultStr = "";
    }

    @FXML
    void onNextButton(MouseEvent event) {
//        waitInfo.setVisible(true);
        Thread th = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("here, the board view is: "+ gameEntity.getCurrView());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Game continues
                            if(gameResult == 2){
                                ShowViews.showGameView(stage, "/ui/mainPage.fxml", gameEntity);
                            }
                            else{
                                ShowViews.showGameView(stage, "/ui/gameResultPage.fxml", gameEntity);
                            }
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

    /**
     * This method eliminate everything excepts spaces
     * @param s
     * @return
     */
    public ArrayList<Character> eliminateSpace(String s){
        ArrayList<Character> ans = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            if((i >= 2 && i <= 20) || i >= 41 && i <= 59){
                if(s.charAt(i) == ' ' || s.charAt(i) == 's' || s.charAt(i) == 'd'
                        || s.charAt(i) == 'c' || s.charAt(i) == 'b' || s.charAt(i) == '*'){
                    ans.add(s.charAt(i));
                }
            }
            else if(i > 22 && i < 39){
                ans.add('.');
            }

        }
        return ans;
    }
    public void strToGrid(String currView){
        String[] oriLines = currView.split("\n");
        System.out.println("ori's size should be 20, but actually is: " + oriLines.length);
        ArrayList<ArrayList<Character>> eachLine = new ArrayList<>();
        for(int i = 0; i < oriLines.length; i++){
            //Title and Space at 0, 1, 2
            if(i != 0  && i != 1 && i != 2 && i != oriLines.length - 1){
                ArrayList<Character> lineNoSplit = eliminateSpace(oriLines[i]);
                eachLine.add(lineNoSplit);
            }
        }
        System.out.println("Eachline's size should be 20, but actually is: " + eachLine.size());
        System.out.println("Eachline's each str should be 20, but actually is: "+ eachLine.get(0).size());

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
                if(eachLine.get(i).get(j) == '.'){
                    continue;
                }
                Rectangle square = new Rectangle(15, 15);
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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void initialize() throws IOException, ClassNotFoundException {
        if (playerId == 0) {
            playerInfo.setText("Turn Result for Player A!");
        } else {
            playerInfo.setText("Turn Result for Player B!");
        }
        //Set turn Result
        currResultStr = gameEntity.recvTurnResultStr();
        turnResult.setText(currResultStr);

        gameResult = gameEntity.recvGameResult();
        System.out.println("When turnResult receives gameResult, it is: " + gameResult);
        gameEntity.recvBoardTextView();
        strToGrid(gameEntity.getCurrView());
    }

}
