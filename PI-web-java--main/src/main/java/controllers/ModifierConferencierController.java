package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.conferencier;
import services.Service_Conferencier;

import java.sql.SQLException;

public class ModifierConferencierController {

    @FXML
    private TextField txtIdEvenement;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtBiographie;

    @FXML
    private TextField txtContactEmail;

    private conferencier selectedConferencier;
    private Service_Conferencier serviceConferencier = new Service_Conferencier();

    public void initData(conferencier conferencier) {
        this.selectedConferencier = conferencier;
        txtIdEvenement.setText(String.valueOf(conferencier.getIdEvenement()));
        txtNom.setText(conferencier.getNom());
        txtBiographie.setText(conferencier.getBiographie());
        txtContactEmail.setText(conferencier.getContactEmail());
    }

    @FXML
    void saveChanges() {
        try {
            int idEvenement = Integer.parseInt(txtIdEvenement.getText());
            String nom = txtNom.getText();
            String biographie = txtBiographie.getText();
            String contactEmail = txtContactEmail.getText();

            serviceConferencier.modifier(selectedConferencier.getIdConferencier(), idEvenement, nom, biographie, contactEmail);
            showAlert("Succès", "Conferencier modifié avec succès.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID Evenement doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la modification du conferencier.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cancel() {
        // Implement logic to return to the previous page
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}