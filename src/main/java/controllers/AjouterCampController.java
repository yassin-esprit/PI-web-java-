package controllers;

import javafx.event.ActionEvent;
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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterCampController {

    @FXML
    private TextField IDnom;

    @FXML
    private TextField IDbudget;

    @FXML
    private DatePicker IDdebut;

    @FXML
    private DatePicker IDfin;

    @FXML
    private ChoiceBox<statut_camp> IDstatut;

    @FXML
    private ComboBox<Integer> IDpub;

    @FXML
    private Button btnAjouter;

    private final ServiceCamp serviceCamp = new ServiceCamp();

    @FXML
    public void initialize() {
        // Initialisation des statuts
        IDstatut.getItems().addAll(statut_camp.values());
        IDstatut.setValue(statut_camp.en_cours); // Valeur par défaut
    }

    public void initData(publicites pub) {
        if (pub != null) {
            IDpub.getItems().add(pub.getId_pub());
            IDpub.setValue(pub.getId_pub());
        } else {
            showAlert("Erreur", "La publicité sélectionnée est invalide.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Récupération des valeurs saisies
            String nomCamp = IDnom.getText().trim();
            String budgetStr = IDbudget.getText().trim();
            LocalDate dateDebut = IDdebut.getValue();
            LocalDate dateFin = IDfin.getValue();
            statut_camp statut = IDstatut.getValue();
            Integer pubId = IDpub.getValue();

            // Vérification des champs
            if (nomCamp.isEmpty() || budgetStr.isEmpty() || dateDebut == null || dateFin == null || statut == null || pubId == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
                return;
            }

            int budget;
            try {
                budget = Integer.parseInt(budgetStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le budget doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }

            // Vérification des dates
            if (dateFin.isBefore(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être postérieure à la date de début.", Alert.AlertType.ERROR);
                return;
            }

            // Création de la campagne
            campagnes_publicitaires campagne = new campagnes_publicitaires(nomCamp, budget, dateDebut.toString(), dateFin.toString(), statut, pubId);
            serviceCamp.ajouter(campagne);

            showAlert("Succès", "Campagne ajoutée avec succès.", Alert.AlertType.INFORMATION);
            afficherCampagnes();
            resetFields();

        } catch (SQLException | IOException e) {
            showAlert("Erreur", "Problème lors de l'ajout de la campagne : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void afficherCampagnes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCamp.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) IDnom.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void resetFields() {
        IDnom.clear();
        IDbudget.clear();
        IDdebut.setValue(null);
        IDfin.setValue(null);
        IDstatut.setValue(statut_camp.en_cours);
        IDpub.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
