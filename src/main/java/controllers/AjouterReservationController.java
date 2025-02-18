package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Reservation_Equipement;
import models.Status_reservation;
import services.ServiceReservation;
import models.Equipement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AjouterReservationController {

    @FXML
    private ComboBox<Integer> comboBoxEvenement; // ComboBox pour l'ID de l'événement

    @FXML
    private ComboBox<Integer> comboBoxEquipement; // ComboBox pour l'ID de l'équipement

    @FXML
    private DatePicker datePickerReservation; // DatePicker pour la date de réservation

    @FXML
    private ComboBox<String> statutChoiceBox; // ComboBox pour le statut de la réservation

    @FXML
    private Button btnAjouter; // Bouton pour ajouter la réservation

    private final ServiceReservation serviceReservation = new ServiceReservation();

    @FXML
    public void initialize() {
        // Initialisation des statuts
        statutChoiceBox.getItems().addAll("attente", "confirme", "annule");
        statutChoiceBox.setValue("attente"); // Valeur par défaut

        chargerEvenements();
        chargerEquipements();
    }

    private void chargerEvenements() {
        try {
            List<Integer> listeEvenements = serviceReservation.LesIdEvenements();
            comboBoxEvenement.getItems().addAll(listeEvenements);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les événements : " + e.getMessage());
        }
    }

    private void chargerEquipements() {
        try {
            List<Integer> listeEquipements = serviceReservation.LesIdEquipements();
            comboBoxEquipement.getItems().addAll(listeEquipements);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les équipements : " + e.getMessage());
        }
    }

    @FXML
    void ajouterReservation(ActionEvent event) {
        try {
            // Récupération des valeurs sélectionnées
            Integer idEvenement = comboBoxEvenement.getValue();
            Integer idEquipement = comboBoxEquipement.getValue();
            String dateReservation = (datePickerReservation.getValue() != null) ? datePickerReservation.getValue().toString() : null;
            String statutStr = statutChoiceBox.getValue();

            // Vérifications des champs
            if (idEvenement == null) {
                showAlert("Erreur", "Veuillez sélectionner un événement.");
                return;
            }

            if (idEquipement == null) {
                showAlert("Erreur", "Veuillez sélectionner un équipement.");
                return;
            }

            if (dateReservation == null || dateReservation.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir la date de réservation.");
                return;
            }

            Status_reservation statut;
            try {
                statut = Status_reservation.valueOf(statutStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "Statut de réservation invalide.");
                return;
            }

            // Création de la réservation
            Reservation_Equipement reservation = new Reservation_Equipement(idEvenement, idEquipement, dateReservation, statut);
            serviceReservation.ajouter(reservation);

            showAlert("Succès", "Réservation ajoutée avec succès.");

            // Rafraîchir la liste des réservations et afficher la nouvelle fenêtre
            afficherReservations();

            // Réinitialisation des champs
            resetFields();

        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout de la réservation : " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Problème lors du chargement de la fenêtre d'affichage : " + e.getMessage());
        }
    }

    private void afficherReservations() throws IOException, SQLException {
        List<Reservation_Equipement> reservations = serviceReservation.recuperer();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
        Parent root = loader.load();

        AfficherReservationController rc = loader.getController();
        rc.setReservations(reservations);

        Stage stage = (Stage) comboBoxEvenement.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private Equipement equipement;

    public void initData(Equipement selectedEquipement) {
        if (selectedEquipement != null) {
            this.equipement = selectedEquipement;
            comboBoxEquipement.setValue(selectedEquipement.getId_eq());
        }
    }

    private void resetFields() {
        comboBoxEvenement.getSelectionModel().clearSelection();
        comboBoxEquipement.getSelectionModel().clearSelection();
        datePickerReservation.setValue(null);
        statutChoiceBox.setValue("attente");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
