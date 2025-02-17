package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.evenement;
import services.ServiceEvenement;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherEvenementController implements Initializable {

    @FXML
    private TableView<evenement> tableEvenements;

    @FXML
    private TableColumn<evenement, Integer> colId;

    @FXML
    private TableColumn<evenement, String> colTitre;

    @FXML
    private TableColumn<evenement, String> colType;

    @FXML
    private TableColumn<evenement, String> colDescription;

    @FXML
    private TableColumn<evenement, String> colDate;

    @FXML
    private TableColumn<evenement, String> colLieu;

    @FXML
    private TableColumn<evenement, Integer> colNbrPlaces;

    @FXML
    private TableColumn<evenement, Integer> colSponsor;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colNbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrplaces"));
        colSponsor.setCellValueFactory(new PropertyValueFactory<>("sponsor"));
    }

    @FXML
    void loadEvenements(ActionEvent event) {
        try {
            // Retrieve the list of events from the database
            List<evenement> evenements = serviceEvenement.recuperer();

            // Convert the list to an ObservableList
            ObservableList<evenement> observableList = FXCollections.observableArrayList(evenements);

            // Set the items in the table
            tableEvenements.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterevenement.fxml"));
            Parent root = loader.load();
            tableEvenements.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du retour : " + e.getMessage());
        }
    }
}