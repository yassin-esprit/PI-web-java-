package Test;

import Controller.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController.setPrimaryStage(primaryStage);
        SceneController.loadSignupScene();
        primaryStage.show();
    }
}