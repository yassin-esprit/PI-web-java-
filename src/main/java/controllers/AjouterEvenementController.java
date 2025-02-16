package controllers;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.evenement;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class AjouterEvenementController {

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


    @FXML
    void ajouterEV(ActionEvent event) throws SQLException {
        String titre = txtTitre.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        LocalDate localDate = txtDate.getValue();

        if (localDate == null) {
            showAlert("Erreur", "Veuillez sélectionner une date valide.", Alert.AlertType.ERROR);
            return;
        }

        String lieu = txtLieu.getText();
        int nbrPlaces;
        int sponsor;

        try {
            nbrPlaces = Integer.parseInt(txtNbrPlaces.getText());
            sponsor = Integer.parseInt(txtSponsor.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Nombre de places et Sponsor doivent être des nombres valides.", Alert.AlertType.ERROR);
            return;
        }

        ServiceEvenement se = new ServiceEvenement();
        evenement e = new evenement(titre, models.type_evenement.valueOf(type), description, localDate, lieu, nbrPlaces, sponsor);

        try {
            se.ajouter(e);

            // Show confirmation popup
            showAlert("Succès", "Événement ajouté avec succès!", Alert.AlertType.INFORMATION);

            // Redirect to AfficherEvenement.fxml after confirmation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent root = loader.load();
            txtTitre.getScene().setRoot(root);

        } catch (SQLException | IOException ex) {
            showAlert("Erreur", "Une erreur s'est produite: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }}
