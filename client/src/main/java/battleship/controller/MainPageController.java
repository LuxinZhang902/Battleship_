package battleship.controller;

import battleship.ShowViews;
import battleship.model.BattleshipGame;
import battleship.model.Game;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainPageController {
    private int playerId;

    private Stage stage;
    private BattleshipGame gameEntity;

    @FXML
    private RadioButton move;

    @FXML
    private AnchorPane boardTextView;

    @FXML
    private Label playerInfo;

    @FXML
    private RadioButton fire;

    @FXML
    private RadioButton sonar;

//    @FXML
//    private Text displayBoard;

    @FXML
    private Label waitInfo;
    @FXML
    private GridPane gridPane;



    @FXML
    void onNextButton(MouseEvent event) throws IOException {
        if(fire.isSelected()){
            ShowViews.showGameView(stage, "/ui/firePage.fxml", gameEntity);
        }
        else if(move.isSelected()){
            ShowViews.showGameView(stage, "/ui/movePage.fxml", gameEntity);
        }
        else if(sonar.isSelected()){
            ShowViews.showGameView(stage, "/ui/sonarPage.fxml", gameEntity);
        }
    }


    public MainPageController(int id, Stage _stage, BattleshipGame _gameEntity) {
        this.playerId = id;
        this.stage = _stage;
        this.gameEntity = _gameEntity;
        playerInfo = new Label();
    }

    public boolean isRowLetter(char c){
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
        for(int i = 0; i < letters.length; i++){
            if(c == letters[i]){
                return true;
            }
        }
        return false;
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
//            System.out.println("curr str: " + eachLine.get(i));
            for (int j = 0; j < eachLine.get(i).size(); j++) {
                if(eachLine.get(i).get(j) == '.'){
                    continue;
                }
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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void initialize() throws IOException, ClassNotFoundException {
        if (playerId == 0) {
            playerInfo.setText("You are Player A. What would you like to do?");
        } else {
            playerInfo.setText("You are Player B. What would you like to do?");
        }
//        displayBoard.setText(gameEntity.getCurrView());
        System.out.println("peint in Main initialize " + gameEntity.getCurrView());
        strToGrid(gameEntity.getCurrView());

    }
}
