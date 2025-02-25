package controllers;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import models.Equipement;
import services.ServiceEquipement;

public class AfficherEquipementController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Equipement> listViewEquipement;

    private ObservableList<Equipement> equipements = FXCollections.observableArrayList();
    private FilteredList<Equipement> filteredEquipements;
    private ServiceEquipement serviceEquipement = new ServiceEquipement();

    public void initialize() {
        // Définir une CellFactory pour personnaliser l'affichage
        listViewEquipement.setCellFactory(param -> new ListCell<Equipement>() {
            @Override
            protected void updateItem(Equipement equipement, boolean empty) {
                super.updateItem(equipement, empty);
                if (empty || equipement == null) {
                    setText(null);
                    setGraphic(null); // Effacer le contenu si l'élément est vide
                } else {
                    // Afficher les informations de l'équipement dans un format personnalisé
                    setText("Nom: " + equipement.getNom() + "\n" +
                            "Description: " + equipement.getDescription() + "\n" +
                            "Quantité disponible: " + equipement.getQuantite_disponible());
                }
            }
        });

        // Initialiser la FilteredList
        filteredEquipements = new FilteredList<>(equipements, p -> true);

        // Lier la FilteredList à la ListView
        listViewEquipement.setItems(filteredEquipements);

        // Charger les équipements
        updateEquipementList();

        // Recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(null); // Appeler handleSearch avec un événement null
        });
    }

    public void setEquipements(List<Equipement> equipementsList) {
        this.equipements.clear();
        this.equipements.addAll(equipementsList);
        filteredEquipements.setPredicate(null); // Réinitialiser le filtre
    }

    public void updateEquipementList() {
        try {
            List<Equipement> equipementsList = serviceEquipement.recuperer();
            setEquipements(equipementsList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la récupération des équipements.");
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

    @FXML
    void handleSearch(ActionEvent actionEvent) {
        String searchText = searchField.getText().toLowerCase();

        // Filtrer la liste en fonction du texte de recherche
        filteredEquipements.setPredicate(equipement -> {
            if (searchText == null || searchText.isEmpty()) {
                return true; // Afficher tous les éléments si le champ de recherche est vide
            }
            return equipement.getNom().toLowerCase().contains(searchText); // Filtrer par nom
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}