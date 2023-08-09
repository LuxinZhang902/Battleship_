package battleship.controller;

import battleship.model.BattleshipGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GameResultPageController {
    @FXML
    private Label playerInfo;

    @FXML
    private AnchorPane boardTextView;

    @FXML
    private Button quitButton;

    @FXML
    private AnchorPane resultPane;

    int playerId;
    Stage stage;
    BattleshipGame gameEntity;


    public GameResultPageController(int id, Stage _stage, BattleshipGame _gameEntity) {
        this.playerId = id;
        this.stage = _stage;
        this.gameEntity = _gameEntity;

    }

    @FXML
    void onQuitButton(MouseEvent event) throws IOException {
        Stage currStage = (Stage) quitButton.getScene().getWindow();
        currStage.close();
        gameEntity.closePipes();
    }

    /**
     * initialize the ChooseActionController
     * show the text of label playerColor
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void initialize(){
        String resultStr = "";
        if (playerId == 0) {
            if(gameEntity.getGameResult() == 0){
                resultStr = "You WIN The Game! \n Game Ends!";
                playerInfo.setText(resultStr);
            }
            else if(gameEntity.getGameResult() == 1){
                resultStr = "You LOSE The Game! \n Game Ends!";
                playerInfo.setText(resultStr);
            }

        } else {
            if(gameEntity.getGameResult() == 1){
                resultStr = "You WIN The Game! \n Game Ends!";
                playerInfo.setText(resultStr);
            }
            else if(gameEntity.getGameResult() == 0){
                resultStr = "You LOSE The Game! \n Game Ends!";
                playerInfo.setText(resultStr);
            }
        }
    }


}
