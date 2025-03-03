package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.conferencier;
import services.Service_Conferencier;

import java.sql.SQLException;

public class AjouterConferencierController {

    @FXML
    private TextField txtIdEvenement;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtBiographie;

    @FXML
    private TextField txtContactEmail;

    private Service_Conferencier serviceConferencier = new Service_Conferencier();

    @FXML
    void ajouterConferencier() {
        try {
            int idEvenement = Integer.parseInt(txtIdEvenement.getText());
            String nom = txtNom.getText();
            String biographie = txtBiographie.getText();
            String contactEmail = txtContactEmail.getText();

            conferencier c = new conferencier(0, idEvenement, nom, biographie, contactEmail);
            serviceConferencier.ajouter(c);

            showAlert("Succès", "Conferencier ajouté avec succès.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID Evenement doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du conferencier.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}