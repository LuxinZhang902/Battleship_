package battleship.controller;

import battleship.model.BattleshipGame;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class GridsController {
    @FXML
    private GridPane gridPane;

    int playerId;
    Stage stage;
    BattleshipGame gameEntity;


    public GridsController(int id, Stage _stage, BattleshipGame _gameEntity) {
        this.playerId = id;
        this.stage = _stage;
        this.gameEntity = _gameEntity;

    }
    public ArrayList<Character> eliminateSpace(String s){
        ArrayList<Character> ans = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != '|' && !Character.isUpperCase(s.charAt(i)) && i != 1 && i != s.length() - 2){
                ans.add(s.charAt(i));
            }
        }
        return ans;
    }

    public void initialize() {
        String currView = gameEntity.getCurrView();
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
        Label headerLabel = new Label("Your Current Board");
        GridPane.setColumnSpan(headerLabel, 10);
        GridPane.setRowIndex(headerLabel, 0);
        GridPane.setColumnIndex(headerLabel, 0);
        gridPane.getChildren().add(headerLabel);

        // Creating column headers
        String[] headers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < headers.length; i++) {
            Label header = new Label(headers[i]);
            header.setAlignment(Pos.CENTER);
            GridPane.setRowIndex(header, 1);
            GridPane.setColumnIndex(header, i+1);
            gridPane.getChildren().add(header);
        }

        // Creating rows and columns for board
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
        for (int i = 0; i < eachLine.size(); i++) {
            Label rowLabel = new Label(Character.toString(letters[i]));
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
            }
        }
    }
}
