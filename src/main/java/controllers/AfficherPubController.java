package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import models.publicites;
import javafx.scene.control.ButtonType;

import models.statut_pub;
import models.type_pub;
import services.ServicePublicite;

import java.sql.SQLException;
import java.util.List;

public class AfficherPubController {

    @FXML
    private TableColumn<publicites, String> ccontenu;

    @FXML
    private TableColumn<publicites, String> cemplacement;

    @FXML
    private TableColumn<publicites, statut_pub> cstatut;

    @FXML
    private TableColumn<publicites, String> ctitte;

    @FXML
    private TableColumn<publicites, type_pub> ctype;

    @FXML
    private Button idModifier;

    @FXML
    private Button idSupprimer;

    @FXML
    private TableView<publicites> idtable;

    private ObservableList<publicites> publicites = FXCollections.observableArrayList();
    private ServicePublicite servicePublicites = new ServicePublicite();

    // Méthode pour passer la liste des publicités
    public void setPublicites(List<publicites> publicitesList) {
        this.publicites.clear();
        this.publicites.addAll(publicitesList);
        idtable.setItems(this.publicites);
    }

    public void updatePublicitesList() {
        try {
            List<publicites> publicitesList = servicePublicites.recuperer();
            if (publicitesList.isEmpty()) {
                showAlert("Aucune publicite", "Il n'y a aucune publicité disponible.");
            }
            setPublicites(publicitesList);
        } catch (SQLException e) {
            showAlert("Erreur de base de données", "Une erreur s'est produite lors de la récupération des publicités.");
            e.printStackTrace();  // Log pour plus de détails
        }
    }


    // Méthode pour la suppression d'une publicité
    @FXML
    void supprimer(ActionEvent event) {
        publicites selectedPub = idtable.getSelectionModel().getSelectedItem();
        if (selectedPub != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette publicité ?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        // Supprimer la publicité via le service
                        servicePublicites.supprimer(selectedPub.getId_pub());
                        updatePublicitesList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Aucune publicité sélectionnée", "Veuillez sélectionner une publicité à supprimer.");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public void modifier(MouseEvent mouseEvent) {
    }
}
