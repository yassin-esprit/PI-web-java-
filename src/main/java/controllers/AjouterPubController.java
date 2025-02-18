package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.publicites;
import models.statut_pub;
import models.type_pub;
import services.ServicePublicite;
import javafx.scene.control.Button;

import java.sql.SQLException;



import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class AjouterPubController {


    private final ServicePublicite sp = new ServicePublicite();

    @FXML
    private TextField idContenu;

    @FXML
    private TextField idEmplacement;

    @FXML
    private ChoiceBox<statut_pub> idStatut;

    @FXML
    private TextField idTitre;

    @FXML
    private ChoiceBox<type_pub> idType;
    public void initialize() {
        idStatut.getItems().addAll(statut_pub.values()); // Ajoute toutes les valeurs de l'énumération
        idType.getItems().addAll(type_pub.values()); // Idem pour l'énumération type_pub
    }


    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        // Récupération des valeurs des champs
        String titre = idTitre.getText();
        String contenu = idContenu.getText();
        String emplacement = idEmplacement.getText();

        // Récupérer directement les objets type_pub et statut_pub sélectionnés dans les ChoiceBox
        type_pub type = idType.getValue();
        statut_pub statut = idStatut.getValue();

        // Vérification des champs
        if (titre.isEmpty() || contenu.isEmpty() || emplacement.isEmpty() || type == null || statut == null) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        // Création de la publicité
        publicites pub = new publicites(titre, type, contenu, emplacement, statut);
        sp.ajouter(pub);

        System.out.println("Publicité ajoutée avec succès !");

        // Récupérer tous les publicités après l'ajout
        List<publicites> publicites = sp.recuperer(); // Assurez-vous que `recuperer` retourne une liste d'objets publicites

        // Passer la liste des publicités au contrôleur de la fenêtre d'affichage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPub.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre d'affichage et lui passer la liste des publicités
            AfficherPubController controller = loader.getController();
            controller.setPublicites(publicites);

            // Changer la scène pour afficher la fenêtre d'affichage
            Stage stage = (Stage) idTitre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // Réinitialiser les champs après l'ajout
           resetFields();

        } catch (IOException e) {
            showAlert("Erreur", "Problème lors du chargement de la fenêtre d'affichage: " + e.getMessage());
        }

        // Fermer la fenêtre actuelle
//((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        // Fermer la fenêtre actuelle avec une vérification de null
        Button sourceButton = (Button) event.getSource();
        Scene scene = sourceButton.getScene();
        if (scene != null && scene.getWindow() != null) {
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        } else {
            System.out.println("The scene or window is null, cannot close the window.");
        }

    }

    private void resetFields() {
        idTitre.clear();
        idContenu.clear();
        idEmplacement.clear();
        idType.setValue(type_pub.image);  // Utilisez un des types valides (par exemple IMAGE)
        idStatut.setValue(statut_pub.active);  // Utilisez un des statuts valides (par exemple ACTIVE)
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
