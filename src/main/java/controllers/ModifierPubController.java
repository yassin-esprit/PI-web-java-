package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.publicites;
import models.statut_pub;
import models.type_pub;
import services.ServicePublicite;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierPubController {

    @FXML
    private TextField txtTitre;

    @FXML
    private ChoiceBox<type_pub> txtType;

    @FXML
    private TextField txtContenu;

    @FXML
    private TextField txtEmplacement;

    @FXML
    private ChoiceBox<statut_pub> txtStatut;

    private publicites selectedPublicite;
    private ServicePublicite servicePublicite = new ServicePublicite();

    // Method to initialize the form with the selected publicite's data
    public void initData(publicites publicite) {
        this.selectedPublicite = publicite;
        txtTitre.setText(publicite.getTitre_pub());
        txtType.setValue(publicite.getType_pub());
        txtContenu.setText(publicite.getContenu_pub());
        txtEmplacement.setText(publicite.getEmplacement_pub());
        txtStatut.setValue(publicite.getStatut_pub());
    }

    @FXML
    void saveChanges() {
        // Get the updated values from the form
        String titre = txtTitre.getText();
        type_pub type = txtType.getValue();
        String contenu = txtContenu.getText();
        String emplacement = txtEmplacement.getText();
        statut_pub statut = txtStatut.getValue();

        // Update the selected publicite
        selectedPublicite.setTitre_pub(titre);
        selectedPublicite.setType_pub(type);
        selectedPublicite.setContenu_pub(contenu);
        selectedPublicite.setEmplacement_pub(emplacement);
        selectedPublicite.setStatut_pub(statut);

        try {
            // Save the changes to the database
            servicePublicite.modifier(
                    selectedPublicite.getId_pub(),
                    titre,
                    type,
                    contenu,
                    emplacement,
                    statut
            );

            // Show success message
            showAlert("Succès", "Publicité modifiée avec succès.", Alert.AlertType.INFORMATION);

            // Redirect to the AfficherPub view
            redirectToAfficherPub();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de la publicité.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cancel() {
        // Redirect to the AfficherPub view without saving changes
        redirectToAfficherPub();
    }

    // Helper method to redirect to the AfficherPub view
    private void redirectToAfficherPub() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherpub.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtTitre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
}