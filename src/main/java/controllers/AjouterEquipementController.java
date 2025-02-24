package controllers;
import javafx.scene.Parent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Equipement;
import models.Status_Equipement;
import services.ServiceEquipement;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AjouterEquipementController {

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField quantiteTotaleTextField;
    @FXML
    private TextField quantiteDisponibleTextField;
    @FXML
    private ChoiceBox<String> statutChoiceBox;

    @FXML
    public void initialize() {
        statutChoiceBox.getItems().addAll("dispo", "indisponible", "maintenance");
        statutChoiceBox.setValue("dispo");
    }

    @FXML
    void ajouterEquipement(ActionEvent event) {
        try {
            String nom = nomTextField.getText().trim();
            String description = descriptionTextField.getText().trim();
            String quantiteTotaleStr = quantiteTotaleTextField.getText().trim();
            String quantiteDisponibleStr = quantiteDisponibleTextField.getText().trim();
            String statutStr = statutChoiceBox.getValue();

            if (nom.isEmpty() || description.isEmpty() || quantiteTotaleStr.isEmpty() || quantiteDisponibleStr.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            int quantiteTotale = 0;
            int quantiteDisponible = 0;
            try {
                quantiteTotale = Integer.parseInt(quantiteTotaleStr);
                quantiteDisponible = Integer.parseInt(quantiteDisponibleStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les quantités.");
                return;
            }

            if (quantiteDisponible > quantiteTotale) {
                showAlert("Erreur", "La quantité disponible ne peut pas dépasser la quantité totale.");
                return;
            }

            Status_Equipement statut = Status_Equipement.valueOf(statutStr);

            Equipement e = new Equipement(nom, description, quantiteDisponible, quantiteTotale, statut);
            ServiceEquipement ep = new ServiceEquipement();
            ep.ajouter(e);

            showAlert("Succès", "Équipement ajouté avec succès.");


            List<Equipement> equipements = ep.recuperer();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEquipement.fxml"));
            Parent root = loader.load();

            AfficherEquipementController ac = loader.getController();
            ac.setEquipements(equipements);

            Stage stage = (Stage) nomTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            resetFields();

        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout de l'équipement: " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Problème lors du chargement de la fenêtre d'affichage: " + e.getMessage());
        }
    }

    private void resetFields() {
        nomTextField.clear();
        descriptionTextField.clear();
        quantiteTotaleTextField.clear();
        quantiteDisponibleTextField.clear();
        statutChoiceBox.setValue("dispo");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
