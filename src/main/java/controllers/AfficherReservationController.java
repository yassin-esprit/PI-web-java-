package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import models.Reservation_Equipement;
import java.util.List;

public class AfficherReservationController {

    @FXML
    private TableView<Reservation_Equipement> reservationTable;

    @FXML
    private TableColumn<Reservation_Equipement, Integer> colIdReservation;

    @FXML
    private TableColumn<Reservation_Equipement, Integer> colIdEquipement;

    @FXML
    private TableColumn<Reservation_Equipement, Integer> colIdEvenement;

    @FXML
    private TableColumn<Reservation_Equipement, String> colDateReservation;

    @FXML
    private TableColumn<Reservation_Equipement, String> colStatut;

    public void setReservations(List<Reservation_Equipement> reservations) {
        colIdReservation.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_reservation()).asObject());
        colIdEquipement.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_equipement()).asObject());
        colIdEvenement.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_evenement()).asObject());
        colDateReservation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateReservation()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus_reservation().name()));

        reservationTable.getItems().setAll(reservations);
    }
}
