package battleship;

import battleship.controller.GridsController;
import battleship.model.BattleshipGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.checkerframework.common.subtyping.qual.Bottom;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class TryGrids extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GridPane gridPane = new GridPane();
//        BattleShipBoard battleShipBoard = new BattleShipBoard<>(10, 20, 'X');
//        BoardTextView view = new BoardTextView(battleShipBoard);
//        String currView = view.displayMyOwnBoard();
//        for(int i = 0; i < currView.length(); i++){
//            if(i == 0)
//        }

//        for (int row = 0; row < 20; row++) {
//            for (int col = 0; col < 10; col++) {
//                Rectangle square = new Rectangle(10, 10);
//                if ((row + col) % 2 == 0) {
//                    square.setFill(Color.WHITE);
//                } else {
//                    square.setFill(Color.BLACK);
//                }
//                square.setStroke(Color.BLACK);
//                gridPane.add(square, col, row);
//            }
//        }
//
//        Scene scene = new Scene(gridPane);
//        stage.setScene(scene);
//        stage.show();

        URL xmlResource = getClass().getResource("/ui/grids.xml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        BattleshipGame game;
//        controllers.put(GridsController.class, new GridsController(game.getPlayerId(), stage, game));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
        GridPane gp = loader.load();
        Scene scene = new Scene(gp, 600, 924);
        //A window is called a stage
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
