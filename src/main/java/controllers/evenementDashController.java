package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.evenement;
import services.ServiceEvenement;
import tools.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class evenementDashController implements Initializable {

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
        try {
            // Retrieve the list of events from the database
            List<evenement> evenements = serviceEvenement.recuperer();

            // Convert the list to an ObservableList
            ObservableList<evenement> observableList = FXCollections.observableArrayList(evenements);

            // Set the items in the table
            tableEvenements.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des événements.", Alert.AlertType.ERROR);
        }
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
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des événements.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void deleteEvenement(ActionEvent event) {
        // Get the selected event from the table
        evenement selectedEvenement = tableEvenements.getSelectionModel().getSelectedItem();

        if (selectedEvenement == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un événement à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        // Confirm deletion with the user
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete the event from the database
                serviceEvenement.supprimer(selectedEvenement.getId());

                // Remove the event from the table
                tableEvenements.getItems().remove(selectedEvenement);

                showAlert("Succès", "Événement supprimé avec succès.", Alert.AlertType.INFORMATION);

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de l'événement.", Alert.AlertType.ERROR);
            }
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

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    @FXML
    void modifyEvenement(ActionEvent event) {
        // Get the selected event from the table
        evenement selectedEvenement = tableEvenements.getSelectionModel().getSelectedItem();

        if (selectedEvenement == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un événement à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Load the modify page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierevenement.fxml"));
            Parent root = loader.load();

            // Pass the selected event to the ModifierEvenementController
            ModifierEvenementController controller = loader.getController();
            controller.initData(selectedEvenement);

            // Switch to the modify page
            tableEvenements.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement de la page de modification.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    void logout(MouseEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        session.cleanUserSession();
        SceneController.loadLoginScene();
    }
    @FXML
    void editpage(MouseEvent event) throws IOException {
        SceneController.loadProfileEditScene();

    }

    @FXML
    public void showStatistics() {
        try {
            // Load the statistics FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statistics.fxml"));
            Parent root = loader.load();

            // Create a new scene and stage for the statistics page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques des Événements");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}