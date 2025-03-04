package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.campagnes_publicitaires;
import models.publicites;
import models.statut_camp;
import services.ServiceCamp;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierCampController {

    @FXML private TextField nomField;
    @FXML private TextField budgetField;
    @FXML private DatePicker dateDebutField;
    @FXML private DatePicker dateFinField;
    @FXML private ChoiceBox<statut_camp> statutField;
    @FXML private ComboBox<publicites> pubField;

    private campagnes_publicitaires campagneToModify;
    private final ServiceCamp serviceCampagne = new ServiceCamp();

    // Méthode d'initialisation des données dans les champs
    public void initData(campagnes_publicitaires campagne) {
        this.campagneToModify = campagne;

        if (campagne != null) {
            nomField.setText(campagne.getNom_camp());
            budgetField.setText(String.valueOf(campagne.getBudget_camp()));
            dateDebutField.setValue(LocalDate.parse(campagne.getDate_debut_camp()));
            dateFinField.setValue(LocalDate.parse(campagne.getDate_fin_camp()));
            statutField.setValue(campagne.getStatut_camp());
           // pubField.setValue(campagne.getPublicite_id());
        }
    }


    private void redirectToAfficherCam() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherCamp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     @FXML
    public void modifierCampagne() {
        if (campagneToModify != null) {
            try {
                // Mise à jour des informations de la campagne avec les nouvelles données
                campagneToModify.setNom_camp(nomField.getText());
                campagneToModify.setBudget_camp((int) Double.parseDouble(budgetField.getText()));
                campagneToModify.setDate_debut_camp(dateDebutField.getValue().toString());
                campagneToModify.setDate_fin_camp(dateFinField.getValue().toString());
                campagneToModify.setStatut_camp(statutField.getValue());
                //campagneToModify.setPublicite(pubField.getValue()); // Mise à jour avec l'objet Publicite

                // Appel du service pour sauvegarder les modifications dans la base de données
                serviceCampagne.modifier(
                    campagneToModify.getId_camp(),
                    campagneToModify.getNom_camp(),
                    campagneToModify.getBudget_camp(),
                    campagneToModify.getDate_debut_camp(),
                    campagneToModify.getDate_fin_camp(),
                    campagneToModify.getStatut_camp(),
                    campagneToModify.getPublicite() // Passage de l'objet Publicite
                );

                redirectToAfficherCam();

                // Fermer la fenêtre de modification après avoir sauvegardé
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException e) {
                afficherErreur("Erreur de saisie", "Le budget doit être un nombre valide.");
            } catch (SQLException e) {
                afficherErreur("Erreur de base de données", "Une erreur s'est produite lors de la mise à jour de la campagne.");
            }
        }
    }


    // Méthode pour afficher les erreurs
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour annuler la modification
    @FXML
    public void annulerModification() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();  // Ferme la fenêtre de modification sans enregistrer les changements
    }
    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
