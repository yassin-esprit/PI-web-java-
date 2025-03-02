package controllers;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import models.Equipement;
import services.ServiceEquipement;


import services.ServiceSheets ;
public class AfficherEquipementController {

    @FXML
    private TextField searchField; // Champ de recherche

    @FXML
    private ListView<Equipement> listViewEquipement; // ListView pour afficher les équipements

    private ObservableList<Equipement> equipements = FXCollections.observableArrayList(); // Liste observable des équipements
    private FilteredList<Equipement> filteredEquipements; // Liste filtrée pour la recherche
    private SortedList<Equipement> sortedEquipements; // Liste triée pour le tri
    private ServiceEquipement serviceEquipement = new ServiceEquipement(); // Service pour gérer les équipements

    public void initialize() {
        // Définir une CellFactory pour personnaliser l'affichage des équipements dans la ListView
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

        // Initialiser la FilteredList pour la recherche
        filteredEquipements = new FilteredList<>(equipements, p -> true);

        // Initialiser la SortedList pour le tri
        sortedEquipements = new SortedList<>(filteredEquipements);

        // Lier la SortedList à la ListView
        listViewEquipement.setItems(sortedEquipements);

        // Charger les équipements au démarrage
        updateEquipementList();

        // Recherche en temps réel : filtrer les résultats à chaque frappe
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(); // Appeler handleSearch pour filtrer les résultats
        });
    }

    /**
     * Met à jour la liste des équipements.
     */
    public void updateEquipementList() {
        try {
            List<Equipement> equipementsList = serviceEquipement.recuperer(); // Récupérer les équipements
            setEquipements(equipementsList); // Mettre à jour la liste observable
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la récupération des équipements.");
        }
    }

    /**
     * Définit la liste des équipements et réinitialise le filtre.
     */
    public void setEquipements(List<Equipement> equipementsList) {
        this.equipements.clear();
        this.equipements.addAll(equipementsList);
        filteredEquipements.setPredicate(null); // Réinitialiser le filtre
    }

    /**
     * Gère la recherche en filtrant les équipements par nom.
     */
    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();

        // Filtrer la liste en fonction du texte de recherche
        filteredEquipements.setPredicate(equipement -> {
            if (searchText == null || searchText.isEmpty()) {
                return true; // Afficher tous les éléments si le champ de recherche est vide
            }
            return equipement.getNom().toLowerCase().contains(searchText); // Filtrer par nom
        });
    }

    /**
     * Trie les équipements par quantité disponible (ordre croissant).
     */
    @FXML
    void trierParQuantite(ActionEvent event) {
        sortedEquipements.setComparator(Comparator.comparingInt(Equipement::getQuantite_disponible));
    }

    /**
     * Ouvre la fenêtre de modification pour l'équipement sélectionné.
     */
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

    /**
     * Supprime l'équipement sélectionné après confirmation.
     */
    @FXML
    void Supprimer(ActionEvent event) {
        Equipement selectedEquipement = listViewEquipement.getSelectionModel().getSelectedItem();
        if (selectedEquipement != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet équipement ?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        serviceEquipement.supprimer(selectedEquipement.getId_eq()); // Supprimer l'équipement
                        updateEquipementList(); // Mettre à jour la liste
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Aucun équipement sélectionné", "Veuillez sélectionner un équipement à supprimer.");
        }
    }

    /**
     * Ouvre la fenêtre de réservation pour l'équipement sélectionné.
     */
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

    /**
     * Affiche une alerte avec un message d'erreur.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }


    @FXML
    private Button syncButton;

    private ServiceEquipement equipementService = new ServiceEquipement();
    private ServiceSheets ServiceSheets = new ServiceSheets();



    // Sync data from database to Google Sheet



    @FXML
    public void handleSyncButton(ActionEvent actionEvent) {
        try {
            // Create an instance of ServiceSheets
            ServiceSheets sheets = new ServiceSheets();

            // Call the syncWithGoogleSheets method and get the spreadsheet URL
            String sheetUrl = sheets.syncWithGoogleSheets();

            // Open the spreadsheet URL in the default web browser
            if (sheetUrl != null && !sheetUrl.isEmpty()) {
                Desktop.getDesktop().browse(new URI(sheetUrl));
            }

            // Show a success message to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Sheets Sync");
            alert.setContentText("Equipment data has been synced with Google Sheets.");
            alert.showAndWait();

        } catch (Exception e) {
            // Show an error message if something goes wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Sheets Sync Failed");
            alert.setContentText("An error occurred while syncing with Google Sheets: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace(); // Print the full stack trace
        }
    }
}