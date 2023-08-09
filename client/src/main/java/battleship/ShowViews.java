package battleship;

import battleship.controller.*;
import battleship.model.BattleshipGame;
import battleship.model.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.internal.net.http.common.Log;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ShowViews {
    public static void showGameView(Stage stage, String xmlPath, BattleshipGame game) throws IOException {
        URL xmlResource = ShowViews.class.getResource(xmlPath);
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(LoginController.class, new LoginController(game.getPlayerId(), stage, game));
        controllers.put(DoPhrasePageController.class, new DoPhrasePageController(game.getPlayerId(), stage, game));
        controllers.put(MainPageController.class, new MainPageController(game.getPlayerId(), stage, game));
        controllers.put(FirePageController.class, new FirePageController(game.getPlayerId(), stage, game));
        controllers.put(MovePageController.class, new MovePageController(game.getPlayerId(), stage, game));
        controllers.put(SonarPageController.class, new SonarPageController(game.getPlayerId(), stage, game));
        controllers.put(TurnResultPageController.class, new TurnResultPageController(game.getPlayerId(), stage, game));
        controllers.put(GameResultPageController.class, new GameResultPageController(game.getPlayerId(), stage, game));
        //Testing
        controllers.put(GridsController.class, new GridsController(game.getPlayerId(), stage, game));

        //TODO: add new controller
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        Scene scene = new Scene(loader.load(), 924, 600);

        stage.setTitle("RISC Game");
        stage.setScene(scene);
        stage.show();
    }
}
