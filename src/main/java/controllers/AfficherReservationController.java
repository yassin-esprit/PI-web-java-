package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
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
    private TableColumn<Reservation_Equipement, Integer> colIdEquipment;

    @FXML
    private TableColumn<Reservation_Equipement, Integer> colIdEvenement;

    @FXML
    private TableColumn<Reservation_Equipement, String> colDateReservation;

    @FXML
    private TableColumn<Reservation_Equipement, String> colStatut;

    private ServiceReservation reservationService;

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        colIdEquipment.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_equipement()).asObject());
        colIdEvenement.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_evenement()).asObject());
        colDateReservation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateReservation()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus_reservation().name()));

        // Initialiser le service
        reservationService = new ServiceReservation();

        // Ajouter les boutons Modifier et Supprimer
        addModifierButtonToTable();
        addSupprimerButtonToTable();

        // Afficher les réservations
        afficherReservations();
    }

    public void afficherReservations() {
        try {
            List<Reservation_Equipement> reservations = reservationService.recuperer();
            setReservations(reservations);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des réservations.");
        }
    }

    public void setReservations(List<Reservation_Equipement> reservations) {
        ObservableList<Reservation_Equipement> observableReservations = FXCollections.observableArrayList(reservations);
        reservationTable.setItems(observableReservations);
    }

    private void addModifierButtonToTable() {
        TableColumn<Reservation_Equipement, Void> colModifier = new TableColumn<>("Modifier");
        Callback<TableColumn<Reservation_Equipement, Void>, TableCell<Reservation_Equipement, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Reservation_Equipement, Void> call(final TableColumn<Reservation_Equipement, Void> param) {
                        final TableCell<Reservation_Equipement, Void> cell = new TableCell<>() {
                            private final Button btn = new Button("Modifier");

                            {
                                btn.setOnAction(event -> {
                                    Reservation_Equipement reservation = getTableView().getItems().get(getIndex());
                                    handleModifierButton(reservation);
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                };

        colModifier.setCellFactory(cellFactory);
        reservationTable.getColumns().add(colModifier);
    }

    private void addSupprimerButtonToTable() {
        TableColumn<Reservation_Equipement, Void> colSupprimer = new TableColumn<>("Supprimer");
        Callback<TableColumn<Reservation_Equipement, Void>, TableCell<Reservation_Equipement, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Reservation_Equipement, Void> call(final TableColumn<Reservation_Equipement, Void> param) {
                        final TableCell<Reservation_Equipement, Void> cell = new TableCell<>() {
                            private final Button btn = new Button("Supprimer");

                            {
                                btn.setOnAction(event -> {
                                    Reservation_Equipement reservation = getTableView().getItems().get(getIndex());
                                    handleSupprimerButton(reservation);
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                };

        colSupprimer.setCellFactory(cellFactory);
        reservationTable.getColumns().add(colSupprimer);
    }

    private void handleModifierButton(Reservation_Equipement reservation) {
        try {
            // Charger la vue ModifierReservation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre ModifierReservation
            ModifierReservationController controller = loader.getController();

            // Passer la réservation sélectionnée au contrôleur de modification
            controller.setReservation(reservation);

            // Afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier Réservation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }


    private void handleSupprimerButton(Reservation_Equipement reservation) {
        if (reservation != null) {
            try {
                reservationService.supprimer(reservation.getId_reservation());
                System.out.println("Réservation supprimée : " + reservation.getId_reservation());
                afficherReservations(); // Rafraîchir la TableView
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de la réservation.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}