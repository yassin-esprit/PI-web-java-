package mains;

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
    public void start(Stage primaryStage) throws IOException {
     //FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCamp.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPub.fxml"));
      // FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPub.fxml"));
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/add.fxml"));


        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ajouter Publicite ");
        primaryStage.show();



    }
//    private void partagerTweetAsync(String tweetContent) {
//        // Exécution de la tâche Twitter dans un nouveau thread
//        new Thread(() -> {
//            try {
//                TwitterService twitterService = new TwitterService();
//                twitterService.partagerSurTwitter(tweetContent);
//
//
//                // Une fois l'opération terminée, retour à l'interface utilisateur (Thread JavaFX)
//                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Succès");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Le tweet a été publié avec succès !");
//                    alert.showAndWait();
//                });
//            } catch (Exception e) {
//                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Erreur");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Une erreur s'est produite lors de l'envoi du tweet.");
//                    alert.showAndWait();
//                });
//            }
//        }).start();
//    }



}
