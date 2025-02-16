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

            // Validation des champs
            if (nom.isEmpty() || description.isEmpty() || quantiteTotaleStr.isEmpty() || quantiteDisponibleStr.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Vérification des valeurs numériques
            int quantiteTotale = 0;
            int quantiteDisponible = 0;
            try {
                quantiteTotale = Integer.parseInt(quantiteTotaleStr);
                quantiteDisponible = Integer.parseInt(quantiteDisponibleStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les quantités.");
                return;
            }

            // Validation de la logique des quantités
            if (quantiteDisponible > quantiteTotale) {
                showAlert("Erreur", "La quantité disponible ne peut pas dépasser la quantité totale.");
                return;
            }

            // Conversion du statut en enum
            Status_Equipement statut = Status_Equipement.valueOf(statutStr);

            // Création de l'équipement
            Equipement e = new Equipement(nom, description, quantiteDisponible, quantiteTotale, statut);
            ServiceEquipement ep = new ServiceEquipement();
            ep.ajouter(e); // Ajouter l'équipement à la base de données

            showAlert("Succès", "Équipement ajouté avec succès.");

            // Récupérer tous les équipements après l'ajout
            List<Equipement> equipements = ep.recuperer(); // Assurez-vous que `recuperer` retourne une liste d'équipements

            // Passer la liste des équipements au contrôleur de la fenêtre d'affichage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEquipement.fxml"));
            Parent root = loader.load();

            // Passer la liste des équipements au contrôleur de la fenêtre d'affichage
            AfficherEquipementController ac = loader.getController();
            ac.setEquipements(equipements); // Assurez-vous que cette méthode existe dans `AfficherEquipementController`

            // Changer la scène pour afficher la fenêtre d'affichage
            Stage stage = (Stage) nomTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // Réinitialiser les champs après l'ajout
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
