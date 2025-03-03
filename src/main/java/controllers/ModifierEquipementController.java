package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import models.Equipement;
import models.Status_Equipement;
import services.ServiceEquipement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ModifierEquipementController {

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField quantiteDisponibleField;

    @FXML
    private TextField quantiteTotaleField;

    @FXML
    private ComboBox<String> statutChoiceBox;

    private Equipement equipement;

    @FXML
    public void initialize() {
        // Ajouter les valeurs possibles du statut dans la ComboBox
        ObservableList<String> statusList = FXCollections.observableArrayList("dispo", "indisponible", "maintenance");
        statutChoiceBox.setItems(statusList);
    }

    public void initData(Equipement eq) {
        this.equipement = eq;

        nomField.setText(eq.getNom());
        descriptionField.setText(eq.getDescription());
        quantiteTotaleField.setText(String.valueOf(eq.getQuantite_totale()));
        quantiteDisponibleField.setText(String.valueOf(eq.getQuantite_disponible()));
        statutChoiceBox.setValue(eq.getStatut().name()); // Convertir l'ENUM en minuscule
    }

    @FXML
    void modifierEquipement(ActionEvent event) {
        try {
            if (equipement != null) {
                if (nomField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                        quantiteTotaleField.getText().isEmpty() || quantiteDisponibleField.getText().isEmpty()) {
                    System.out.println("Veuillez remplir tous les champs.");
                    return;
                }

                int quantiteTotale = Integer.parseInt(quantiteTotaleField.getText());
                int quantiteDisponible = Integer.parseInt(quantiteDisponibleField.getText());

                if (quantiteTotale < 0 || quantiteDisponible < 0 || quantiteDisponible > quantiteTotale) {
                    System.out.println("Les quantités doivent être valides.");
                    return;
                }

                equipement.setNom(nomField.getText());
                equipement.setDescription(descriptionField.getText());
                equipement.setQuantite_totale(quantiteTotale);
                equipement.setQuantite_disponible(quantiteDisponible);

                String statutValue = statutChoiceBox.getValue();
                if (statutValue != null && Status_Equipement.valueOf(statutValue) != null) {
                    equipement.setStatut(Status_Equipement.valueOf(statutValue));
                } else {
                    System.out.println("Statut non valide.");
                    return;
                }

                ServiceEquipement service = new ServiceEquipement();
                service.modifier(
                        equipement.getId_eq(),
                        equipement.getNom(),
                        equipement.getDescription(),
                        equipement.getQuantite_totale(),
                        equipement.getQuantite_disponible(),
                        equipement.getStatut()
                );

                System.out.println("Équipement modifié avec succès !");
                fermerFenetre();
            } else {
                System.out.println("Aucun équipement sélectionné.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer des nombres valides pour les quantités.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void annulerModification(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    public void handleLogout(ActionEvent actionEvent) {
        try {
            // Charger la page de connexion (login)
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Use actionEvent here

            // Changer la scène pour afficher la page de connexion
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
