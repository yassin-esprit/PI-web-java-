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
    private TextField txtIdEvenement;

    @FXML
    private TextField txtIdEquipement;

    @FXML
    private DatePicker datePickerReservation;

    @FXML
    private ComboBox<String> statutChoiceBox;

    private Equipement equipement; // Déclaration de la variable equipement

    @FXML
    public void initialize() {
        // Ajout des statuts possibles
        statutChoiceBox.getItems().addAll("attente", "confirme", "annule");
        statutChoiceBox.setValue("attente"); // Valeur par défaut
    }

    @FXML
    void ajouterReservation(ActionEvent event) {
        try {
            // Récupération des valeurs des champs
            String idEvenementStr = txtIdEvenement.getText().trim();
            String idEquipementStr = txtIdEquipement.getText().trim();
            String dateReservation = (datePickerReservation.getValue() != null) ? datePickerReservation.getValue().toString() : "";
            String statutStr = statutChoiceBox.getValue();

            // Vérification des champs obligatoires
            if (idEvenementStr.isEmpty() || idEquipementStr.isEmpty() || dateReservation.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Vérification des ID numériques
            int idEvenement, idEquipement;
            try {
                idEvenement = Integer.parseInt(idEvenementStr);
                idEquipement = Integer.parseInt(idEquipementStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les identifiants.");
                return;
            }

            // Conversion du statut en enum
            Status_reservation statut;
            try {
                statut = Status_reservation.valueOf(statutStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "Statut de réservation invalide.");
                return;
            }

            // Création de la réservation
            Reservation_Equipement reservation = new Reservation_Equipement(idEvenement, idEquipement, dateReservation, statut);
            ServiceReservation serviceReservation = new ServiceReservation();
            serviceReservation.ajouter(reservation); // Ajouter à la base de données

            showAlert("Succès", "Réservation ajoutée avec succès.");

            // Récupérer toutes les réservations après l'ajout
            List<Reservation_Equipement> reservations = serviceReservation.recuperer();
            System.out.println("Nombre de réservations récupérées : " + reservations.size());

            // Pour chaque réservation, affiche les détails pour débogage
            for (Reservation_Equipement r : reservations) {
                System.out.println(r); // Affiche l'objet pour vérifier le contenu
            }

            // Chargement de la nouvelle fenêtre d'affichage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            // Passer la liste des réservations au contrôleur
            AfficherReservationController rc = loader.getController();
            rc.setReservations(reservations);

            // Changer la scène pour afficher les réservations
            Stage stage = (Stage) txtIdEvenement.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // Réinitialiser les champs après l'ajout
            resetFields();

        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout de la réservation : " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Problème lors du chargement de la fenêtre d'affichage : " + e.getMessage());
        }
    }

    // Réinitialiser les champs après l'ajout
    private void resetFields() {
        txtIdEvenement.clear();
        txtIdEquipement.clear();
        datePickerReservation.setValue(null);
        statutChoiceBox.setValue("attente");
    }

    public void initData(Equipement selectedEquipement) {
        this.equipement = selectedEquipement;
        txtIdEquipement.setText(String.valueOf(selectedEquipement.getId_eq()));
    }

    // Afficher une boîte de dialogue d'alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}