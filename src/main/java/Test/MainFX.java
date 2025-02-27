package Test;

import Controller.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set the primaryStage using the SceneController
        SceneController.setPrimaryStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/add.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter ");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
