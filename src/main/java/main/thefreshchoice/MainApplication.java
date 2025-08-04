package main.thefreshchoice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainApplication extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/main/thefreshchoice/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("The Fresh Choice");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint(""); //menghilangkan tulisan press esc
        stage.show();
        System.out.println();
    }

    public static void main(String[] args) {
        launch();
    }
}