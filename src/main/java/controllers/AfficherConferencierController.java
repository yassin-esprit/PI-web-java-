package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.conferencier;
import services.Service_Conferencier;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherConferencierController implements Initializable {

    @FXML
    private TableView<conferencier> tableConferenciers;

    @FXML
    private TableColumn<conferencier, Integer> colIdConferencier;

    @FXML
    private TableColumn<conferencier, String> colIdEvenement;

    @FXML
    private TableColumn<conferencier, String> colNom;

    @FXML
    private TableColumn<conferencier, String> colBiographie;

    @FXML
    private TableColumn<conferencier, String> colContactEmail;

    private Service_Conferencier serviceConferencier = new Service_Conferencier();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the table columns
        colIdConferencier.setCellValueFactory(new PropertyValueFactory<>("idConferencier"));
        // Change the column to show the titre of the evenement
        colIdEvenement.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTitreEvenement()); // Make sure this is correct
        });

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colBiographie.setCellValueFactory(new PropertyValueFactory<>("biographie"));
        colContactEmail.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
    }

    @FXML
    void loadConferenciers() {
        try {
            List<conferencier> conferenciers = serviceConferencier.recuperer();
            ObservableList<conferencier> observableList = FXCollections.observableArrayList(conferenciers);
            tableConferenciers.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des conferenciers.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void deleteConferencier() {
        conferencier selectedConferencier = tableConferenciers.getSelectionModel().getSelectedItem();

        if (selectedConferencier == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un conferencier à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce conferencier ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceConferencier.supprimer(selectedConferencier.getIdConferencier());
                loadConferenciers();
                showAlert("Succès", "Conferencier supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression du conferencier.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void modifyConferencier(ActionEvent event) {
        // Get the selected conferencier from the table
        conferencier selectedConferencier = tableConferenciers.getSelectionModel().getSelectedItem();

        if (selectedConferencier == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un conferencier à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Load the modify page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierconferencier.fxml"));
            Parent root = loader.load();

            // Pass the selected conferencier to the ModifierConferencierController
            ModifierConferencierController controller = loader.getController();
            controller.initData(selectedConferencier);

            // Switch to the modify page
            tableConferenciers.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement de la page de modification.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterconferencier.fxml"));
            Parent root = loader.load();
            tableConferenciers.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}