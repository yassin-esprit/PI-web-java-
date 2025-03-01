package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.campagnes_publicitaires;
import models.publicites;
import services.ServiceCamp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherCampController {

    @FXML
    private Button btnReload;

    @FXML
    private TableColumn<?, ?> colBudgetCamp;

    @FXML
    private TableColumn<?, ?> colDateDebut;

    @FXML
    private TableColumn<?, ?> colDateFin;

    @FXML
    private TableColumn<?, ?> colNomCamp;

    @FXML
    private TableColumn<?, ?> colStatut;

//    @FXML
//    private TableColumn<?, ?> colPublicite; // Nouvelle colonne pour la publicité

    @FXML
    private TableView<campagnes_publicitaires> tableCampagnes;

    private final ServiceCamp serviceCampagne = new ServiceCamp();
    private ObservableList<campagnes_publicitaires> campagnesPublicitaires = FXCollections.observableArrayList();

    public void initialize() {
        // Associer les colonnes aux attributs du modèle
        colNomCamp.setCellValueFactory(new PropertyValueFactory<>("nom_camp"));
        colBudgetCamp.setCellValueFactory(new PropertyValueFactory<>("budget_camp"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("date_debut_camp"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("date_fin_camp"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut_camp"));
        //colPublicite.setCellValueFactory(new PropertyValueFactory<>("publicite")); // Afficher la publicité

        // Charger les données
        chargerCampagnes();
    }

    public void chargerCampagnes() {
        try {
            List<campagnes_publicitaires> campagnesPublicitaires = serviceCampagne.recuperer();
            ObservableList<campagnes_publicitaires> observableList = FXCollections.observableArrayList(campagnesPublicitaires);
            tableCampagnes.setItems(observableList);

            // Vérification des données chargées
            if (observableList.isEmpty()) {
                afficherErreur("Aucune donnée trouvée.");
            } else {
                System.out.println("Données chargées avec succès : " + observableList.size() + " éléments.");
            }
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des campagnes : " + e.getMessage());
        }
    }

    public void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCamp(List<campagnes_publicitaires> CampList) {
        this.campagnesPublicitaires.clear();
        this.campagnesPublicitaires.addAll(CampList);
        this.tableCampagnes.setItems(this.campagnesPublicitaires);
    }

    public void updateCampList() {
        try {
            List<campagnes_publicitaires> CampList = this.serviceCampagne.recuperer();
            if (CampList.isEmpty()) {
                this.showAlert("Aucune campagne", "Il n'y a aucune campagne disponible.");
            }

            this.setCamp(CampList);
        } catch (SQLException var2) {
            this.showAlert("Erreur de base de données", "Une erreur s'est produite lors de la récupération des campagnes.");
            var2.printStackTrace();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        campagnes_publicitaires selectedCamp = tableCampagnes.getSelectionModel().getSelectedItem();
        if (selectedCamp != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette campagne ?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        serviceCampagne.supprimer(selectedCamp.getId_camp());
                        updateCampList();
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Aucune campagne sélectionnée", "Veuillez sélectionner une campagne à supprimer.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    private void modifier(ActionEvent event) {
        campagnes_publicitaires selectedCampagne = tableCampagnes.getSelectionModel().getSelectedItem();
        if (selectedCampagne != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCamp.fxml"));
                Parent root = loader.load();

                ModifierCampController controller = loader.getController();
                controller.initData(selectedCampagne); // Envoie la campagne sélectionnée au contrôleur de modification

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Campagne");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune campagne sélectionnée", "Veuillez sélectionner une campagne à modifier.");
        }
    }

    @FXML
    void afficherStatistiques(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatCamp.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et initialiser les données
            StatCampController controller = loader.getController();
            controller.afficherStatistiques();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques des Campagnes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}