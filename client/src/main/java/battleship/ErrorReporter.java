package battleship;

import javafx.scene.control.Alert;

public class ErrorReporter implements Thread.UncaughtExceptionHandler {
    /**
     * show player the error information
     * @param thread
     * @param error
     */
    @Override
    public void uncaughtException(Thread thread, Throwable error) {
        error.printStackTrace();
        while(error.getCause() != null) {
            error = error.getCause();
        }
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setHeaderText(error.getClass().getName());
        dialog.setContentText(error.getMessage());
        dialog.showAndWait();
    }
}