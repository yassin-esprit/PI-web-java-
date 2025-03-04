package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PopUpAdController {

    @FXML
    private ImageView adImage;

    @FXML
    private Button closeButton;

    @FXML
    private void closePopUp() {
        // Get the stage (window) and close it
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}