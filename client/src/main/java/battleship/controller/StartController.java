package battleship.controller;


import battleship.Board;
import battleship.ShowViews;
import battleship.model.BattleshipGame;
import battleship.model.Game;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


import javafx.scene.image.ImageView;
import sun.nio.cs.ext.GBK;

public class StartController {

    @FXML
    private Button startButton;
    @FXML
    private ImageView startImage;
    @FXML
    private Label waitInfo;

    BattleshipGame gameEntity;
    Stage stage;

    public StartController(Stage _stage, BattleshipGame _gameEntity) {
        this.gameEntity = _gameEntity;
        this.stage = _stage;
    }

    /**
     * if user click the start button, connect to the server and start the game
     * @param ae
     */
    @FXML
    public void onStartButton(ActionEvent ae){
        waitInfo.setVisible(true);
        Thread th = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    gameEntity = new BattleshipGame();
                    System.out.println("initialized the game!");

                    //Receive ID
                    int playerID = gameEntity.recvPlayerId();
                    System.out.println("playerId is: " + playerID);
                    if (playerID != 0 && playerID != 1) {
                        throw new Exception("Failed to receive valid playerId!");
                    }

                    //Sending String
                    gameEntity.sendString("Hi, Player " + gameEntity.getPlayerId() + " has successfully connected with you!");

                    System.out.println("New turn: updated new board as below!");
//                    gameEntity.recvBoardTextView();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ShowViews.showGameView(stage, "/ui/login.fxml", gameEntity);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

        });
        th.setDaemon(true);
        th.start();
    }


}
