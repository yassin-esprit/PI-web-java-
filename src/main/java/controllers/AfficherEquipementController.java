package controllers;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Equipement;
import services.ServiceEquipement;


public class AfficherEquipementController {

    @FXML
    private ListView<Equipement> listViewEquipement;

    private ObservableList<Equipement> equipements = FXCollections.observableArrayList();
    private ServiceEquipement serviceEquipement = new ServiceEquipement();

    public void setEquipements(List<Equipement> equipementsList) {
        this.equipements.clear();
        this.equipements.addAll(equipementsList);
        listViewEquipement.setItems(this.equipements);
    }

    public void updateEquipementList() {
        try {
            List<Equipement> equipementsList = serviceEquipement.recuperer();
            setEquipements(equipementsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void GoToModifier(ActionEvent event) {
        Equipement selectedEquipement = listViewEquipement.getSelectionModel().getSelectedItem();
        if (selectedEquipement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEquipement.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                ModifierEquipementController controller = loader.getController();
                controller.initData(selectedEquipement); // Passer l'équipement sélectionné

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun équipement sélectionné", "Veuillez sélectionner un équipement à modifier.");
        }
    }

    @FXML
    void Supprimer(ActionEvent event) {
        Equipement selectedEquipement = listViewEquipement.getSelectionModel().getSelectedItem();
        if (selectedEquipement != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet équipement ?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        serviceEquipement.supprimer(selectedEquipement.getId_eq());
                        updateEquipementList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Aucun équipement sélectionné", "Veuillez sélectionner un équipement à supprimer.");
        }
    }

    @FXML
    void goToReservation(ActionEvent event) {
        Equipement selectedEquipement = listViewEquipement.getSelectionModel().getSelectedItem();
        if (selectedEquipement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                AjouterReservationController controller = loader.getController();
                controller.initData(selectedEquipement); // Passer l'équipement sélectionné

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun équipement sélectionné", "Veuillez sélectionner un équipement à réserver.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
