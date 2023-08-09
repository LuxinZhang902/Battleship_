package battleship.controller;

import battleship.ShowViews;
import battleship.model.BattleshipGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLogin;

    @FXML
    private Label userName;

    @FXML
    private Label userName1;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    int playerId;
    Stage stage;
    BattleshipGame gameEntity;


    public LoginController(int id, Stage _stage, BattleshipGame _gameEntity) {
        this.playerId = id;
        this.stage = _stage;
        this.gameEntity = _gameEntity;

    }
    @FXML
    void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        if(username.getText().toString().equals("player") && password.getText().toString().equals("1234")){
            errorLogin.setText("Success!");
            ShowViews.showGameView(stage, "/ui/doPhrasePage.fxml", gameEntity);
        }
        else if(username.getText().isEmpty() && password.getText().isEmpty()){
            errorLogin.setText("Please Enter Both Username and the Password!");
        }
        else{
            errorLogin.setText("Wrong Username or Password!");
        }
    }
}
