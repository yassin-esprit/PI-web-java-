package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.publicites;
import models.statut_pub;
import models.type_pub;
import services.ServicePublicite;

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
    private Button idDisplay; // Add the Display button
    @FXML
    private TableView<publicites> idtable;
    private ObservableList<publicites> publicites = FXCollections.observableArrayList();
    private ServicePublicite servicePublicites = new ServicePublicite();

    public AfficherPubController() {
    }

    public void setPublicites(List<publicites> publicitesList) {
        this.publicites.clear();
        this.publicites.addAll(publicitesList);
        this.idtable.setItems(this.publicites);
    }

    public void updatePublicitesList() {
        try {
            List<publicites> publicitesList = this.servicePublicites.recuperer();
            if (publicitesList.isEmpty()) {
                this.showAlert("Aucune publicite", "Il n'y a aucune publicit\u00e9 disponible.");
            }

            this.setPublicites(publicitesList);
        } catch (SQLException var2) {
            this.showAlert("Erreur de base de donn\u00e9es", "Une erreur s'est produite lors de la r\u00e9cup\u00e9ration des publicit\u00e9s.");
            var2.printStackTrace();
        }

    }

    @FXML
    void supprimer(ActionEvent event) {
        publicites selectedPub = (publicites)this.idtable.getSelectionModel().getSelectedItem();
        if (selectedPub != null) {
            Alert confirmation = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette publicit\u00e9 ?", new ButtonType[]{ButtonType.YES, ButtonType.NO});
            confirmation.showAndWait().ifPresent((response) -> {
                if (response == ButtonType.YES) {
                    try {
                        this.servicePublicites.supprimer(selectedPub.getId_pub());
                        this.updatePublicitesList();
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }

            });
        } else {
            this.showAlert("Aucune publicit\u00e9 s\u00e9lectionn\u00e9e", "Veuillez s\u00e9lectionner une publicit\u00e9 \u00e0 supprimer.");
        }

    }
    @FXML
    public void initialize() {
        // Bind the columns to the publicites model properties
        ctitte.setCellValueFactory(new PropertyValueFactory<>("titre_pub"));
        ctype.setCellValueFactory(new PropertyValueFactory<>("type_pub"));
        ccontenu.setCellValueFactory(new PropertyValueFactory<>("contenu_pub"));
        cemplacement.setCellValueFactory(new PropertyValueFactory<>("emplacement_pub"));
        cstatut.setCellValueFactory(new PropertyValueFactory<>("statut_pub"));

        // Load the data into the table
        updatePublicitesList();
    }
    @FXML
    void displayPublicites(ActionEvent event) {
        this.updatePublicitesList();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void modifier(ActionEvent event) {
        publicites selectedPub = idtable.getSelectionModel().getSelectedItem();
        if (selectedPub != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierpub.fxml"));
                Parent root = loader.load();
                ModifierPubController controller = loader.getController();
                controller.initData(selectedPub); // Pass the selected publicite to the modifier controller
                Stage stage = (Stage) idtable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune publicité sélectionnée", "Veuillez sélectionner une publicité à modifier.");
        }
    }
}