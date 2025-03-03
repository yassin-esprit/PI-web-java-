package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Reservation_Equipement;
import models.Status_reservation;
import services.ServiceReservation;
import models.Equipement;
import java.sql.SQLException;
import java.util.List;
import models.evenement;
public class ModifierReservationController {

    @FXML
    private ComboBox<String> comboBoxEquipement; // Remplacer TextField par ComboBox pour le nom de l'équipement

    @FXML
    private ComboBox<String> comboBoxEvenement;

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

    private Runnable onModificationComplete;

    @FXML
    public void initialize() {
        reservationService = new ServiceReservation();

        statutChoiceBox.getItems().addAll("attente", "confirme", "annule");

        chargerEquipements(); // Charger les équipements dans le ComboBox
    }

    private void chargerEquipements() {
        try {
            // Charger les noms d'équipements dans le ComboBox
            List<Equipement> listeEquipements = reservationService.LesEquipements();
            for (Equipement equipement : listeEquipements) {
                comboBoxEquipement.getItems().add(equipement.getNom()); // Ajouter le nom de l'équipement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setReservation(Reservation_Equipement reservation) {
        this.reservation = reservation;

        // Afficher l'équipement par son nom dans le ComboBox
        comboBoxEquipement.setValue(reservation.getEquipement().getNom());
        comboBoxEvenement.setValue(reservation.getEvenement().getTitre());
        datePickerReservation.setValue(java.time.LocalDate.parse(reservation.getDateReservation()));
        statutChoiceBox.setValue(reservation.getStatus_reservation().name().toLowerCase());
    }

    public void setOnModificationComplete(Runnable onModificationComplete) {
        this.onModificationComplete = onModificationComplete;
    }

    @FXML
    private void Enregistrer(ActionEvent event) {
        try {
            // Récupérer le nom de l'équipement sélectionné dans le ComboBox
            String nomEquipement = comboBoxEquipement.getValue();
            String TitreEvenement = comboBoxEvenement.getValue();
            String dateReservation = datePickerReservation.getValue().toString();
            Status_reservation statut = Status_reservation.valueOf(statutChoiceBox.getValue().toUpperCase());

            // Récupérer l'objet Equipement en fonction du nom sélectionné
            Equipement equipement = reservationService.getEquipementParNom(nomEquipement);
            evenement evenement = reservationService.getEvenementParNom(TitreEvenement);


            if (equipement == null) {
                // Afficher une erreur si l'équipement n'a pas été trouvé
                System.out.println("Équipement introuvable");
                return;
            }

            // Mettre à jour la réservation
            reservation.setEquipement(equipement);
            reservation.setEvenement(evenement);
            reservation.setDateReservation(dateReservation);
            reservation.setStatus_reservation(statut);

            // Modifier la réservation
            reservationService.modifier(
                    reservation.getId_reservation(),
                    reservation.getEquipement(),
                    reservation.getEvenement(),
                    reservation.getDateReservation(),
                    reservation.getStatus_reservation()
            );

            // Si une fonction de callback a été définie, l'exécuter
            if (onModificationComplete != null) {
                onModificationComplete.run();
            }

            // Fermer la fenêtre après la modification
            Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la modification de la réservation.");
        }
    }

    @FXML
    private void Annuler(ActionEvent event) {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    public void handleLogout(ActionEvent actionEvent) {
        try {
            // Charger la page de connexion (login)
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Use actionEvent here

            // Changer la scène pour afficher la page de connexion
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
