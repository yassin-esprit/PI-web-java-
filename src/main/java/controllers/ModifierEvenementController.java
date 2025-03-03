package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.evenement;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ModifierEvenementController {

    @FXML
    private TextField txtTitre;

    @FXML
    private TextField txtType;

    @FXML
    private TextField txtDescription;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtLieu;

    @FXML
    private TextField txtNbrPlaces;

    @FXML
    private TextField txtSponsor;

    private evenement selectedEvenement;
    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    // Method to initialize the form with the selected event's data
    public void initData(evenement evenement) {
        this.selectedEvenement = evenement;
        txtTitre.setText(evenement.getTitre());
        txtType.setText(evenement.getType().name());
        txtDescription.setText(evenement.getDescription());
        txtDate.setValue(evenement.getDate());
        txtLieu.setText(evenement.getLieu());
        txtNbrPlaces.setText(String.valueOf(evenement.getNbrplaces()));
        txtSponsor.setText(String.valueOf(evenement.getSponsor()));
    }

    @FXML
    void saveChanges() {
        // Get the updated values from the form
        String titre = txtTitre.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        LocalDate date = txtDate.getValue();
        String lieu = txtLieu.getText();
        int nbrPlaces = Integer.parseInt(txtNbrPlaces.getText());
        int sponsor = Integer.parseInt(txtSponsor.getText());

        // Update the selected event
        selectedEvenement.setTitre(titre);
        selectedEvenement.setType(models.type_evenement.valueOf(type));
        selectedEvenement.setDescription(description);
        selectedEvenement.setDate(date);
        selectedEvenement.setLieu(lieu);
        selectedEvenement.setNbrplaces(nbrPlaces);
        selectedEvenement.setSponsor(sponsor);

        try {
            // Save the changes to the database
            serviceEvenement.modifier(selectedEvenement.getId(), titre, type, description, java.sql.Date.valueOf(date), lieu, nbrPlaces, sponsor);

            // Show success message
            showAlert("Succès", "Événement modifié avec succès.", Alert.AlertType.INFORMATION);

            // Redirect to the AfficherEvenement view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementdash.fxml"));
            Parent root = loader.load();
            txtTitre.getScene().setRoot(root);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de l'événement.", Alert.AlertType.ERROR);

        }
    }

    @FXML
    void cancel() {
        try {
            // Redirect to the AfficherEvenement view without saving changes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenementdash.fxml"));
            Parent root = loader.load();
            txtTitre.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}