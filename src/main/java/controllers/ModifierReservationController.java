package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Reservation_Equipement;
import models.Status_reservation;
import services.ServiceReservation;

import java.sql.SQLException;

public class ModifierReservationController {

    @FXML
    private TextField txtIdEquipement;

    @FXML
    private TextField txtIdEvenement;

    @FXML
    private DatePicker datePickerReservation;

    @FXML
    private ComboBox<String> statutChoiceBox;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnAnnuler;

    private Reservation_Equipement reservation;
    private ServiceReservation reservationService;

    @FXML
    public void initialize() {
        // Initialiser le service
        reservationService = new ServiceReservation();

        // Ajouter les statuts possibles dans la ComboBox
        statutChoiceBox.getItems().addAll("attente", "confirme", "annule");
    }

    // Méthode pour charger les données de la réservation à modifier
    public void setReservation(Reservation_Equipement reservation) {
        this.reservation = reservation;

        // Remplir les champs avec les données de la réservation
        txtIdEquipement.setText(String.valueOf(reservation.getId_equipement()));
        txtIdEvenement.setText(String.valueOf(reservation.getId_evenement()));
        datePickerReservation.setValue(java.time.LocalDate.parse(reservation.getDateReservation()));
        statutChoiceBox.setValue(reservation.getStatus_reservation().name().toLowerCase());
    }

    // Méthode pour gérer l'action du bouton Enregistrer
    @FXML
    private void Enregistrer(ActionEvent event) {
        try {
            // Mettre à jour les données de la réservation
            reservation.setId_equipement(Integer.parseInt(txtIdEquipement.getText()));
            reservation.setId_evenement(Integer.parseInt(txtIdEvenement.getText()));
            reservation.setDateReservation(datePickerReservation.getValue().toString());
            reservation.setStatus_reservation(Status_reservation.valueOf(statutChoiceBox.getValue().toUpperCase()));

            // Enregistrer les modifications dans la base de données
            reservationService.modifier(
                    reservation.getId_reservation(),
                    reservation.getId_equipement(),
                    reservation.getId_evenement(),
                    reservation.getDateReservation(),
                    reservation.getStatus_reservation()
            );


            // Fermer la fenêtre de modification
            Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la modification de la réservation.");
        }
    }

    // Méthode pour gérer l'action du bouton Annuler
    @FXML
    private void Annuler(ActionEvent event) {
        // Fermer la fenêtre sans sauvegarder
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}