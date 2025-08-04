package Helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.concurrent.atomic.AtomicBoolean;

public class MessageBox {
    public void alertInfo(String message){
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Informasi");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.initOwner(main.thefreshchoice.MainApplication.primaryStage);
        infoAlert.initModality(Modality.WINDOW_MODAL);
        infoAlert.showAndWait();
    }
    public void alertWarning(String message){
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Peringatan");
        warningAlert.setHeaderText(null);
        warningAlert.setContentText(message);
        warningAlert.initOwner(main.thefreshchoice.MainApplication.primaryStage);
        warningAlert.initModality(Modality.WINDOW_MODAL);
        warningAlert.showAndWait();
    }

    public void alertError(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Kesalahan");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.initOwner(main.thefreshchoice.MainApplication.primaryStage);
        errorAlert.initModality(Modality.WINDOW_MODAL);
        errorAlert.showAndWait();
    }
    public boolean alertConfirm(String message){
        AtomicBoolean result = new AtomicBoolean(false);
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText(message);
        confirmAlert.initOwner(main.thefreshchoice.MainApplication.primaryStage);
        confirmAlert.initModality(Modality.WINDOW_MODAL);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result.set(true);
            } else {
                result.set(false);
            }
        });
        return result.get();
    }
}