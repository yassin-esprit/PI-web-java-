package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Reservation_Equipement;
import services.ServiceReservation;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AfficherReservationController {

    @FXML
    private TableView<Reservation_Equipement> reservationTable;

    @FXML
    private TableColumn<Reservation_Equipement, String> colNomEquipement;

    @FXML
    private TableColumn<Reservation_Equipement, String> colTitreEvenement;

    @FXML
    private TableColumn<Reservation_Equipement, String> colDateReservation;

    @FXML
    private TableColumn<Reservation_Equipement, String> colStatut;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private ServiceReservation reservationService;

    private Reservation_Equipement selectedReservation;

    @FXML
    public void initialize() {
        colNomEquipement.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEquipement().getNom())
        );
        colTitreEvenement.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEvenement().getTitre())
        );
        colDateReservation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateReservation())
        );
        colStatut.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus_reservation().name())
        );

        reservationService = new ServiceReservation();
        afficherReservations();

        reservationTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedReservation = newValue;
            updateButtonsState();
        });
    }

    public void afficherReservations() {
        try {
            List<Reservation_Equipement> reservations = reservationService.recuperer();
            setReservations(reservations);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la récupération des réservations.");
        }
    }

    public void setReservations(List<Reservation_Equipement> reservations) {
        ObservableList<Reservation_Equipement> observableReservations = FXCollections.observableArrayList(reservations);
        reservationTable.setItems(observableReservations);
    }

    @FXML
    private void handleModifierButton() {
        if (selectedReservation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
                Parent root = loader.load();

                ModifierReservationController controller = loader.getController();
                controller.setReservation(selectedReservation);
                controller.setOnModificationComplete(() -> afficherReservations());

                Stage stage = new Stage();
                stage.setTitle("Modifier Réservation");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réservation à modifier.");
        }
    }

    @FXML
    private void handleSupprimerButton() {
        if (selectedReservation != null) {
            try {
                reservationService.supprimer(selectedReservation.getId_reservation());
                afficherReservations(); // Rafraîchir la TableView
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de la réservation.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réservation à supprimer.");
        }
    }

    private void updateButtonsState() {
        boolean isSelected = selectedReservation != null;
        btnModifier.setDisable(!isSelected);
        btnSupprimer.setDisable(!isSelected);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
