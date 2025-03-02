package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Reservation_Equipement;
import models.Status_reservation;
import services.ServiceReservation;
import models.Equipement;
import models.evenement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AjouterReservationController {

    @FXML
    private ComboBox<String> comboBoxEvenement;

    @FXML
    private ComboBox<String> comboBoxEquipement;

    @FXML
    private DatePicker datePickerReservation;

    @FXML
    private ComboBox<String> statutChoiceBox;

    @FXML
    private Button btnAjouter;

    private ServiceReservation serviceReservation = new ServiceReservation();

    private Equipement equipement; // Equipement à réserver

//    @FXML
//    private void initialize() {
//        statutChoiceBox.getItems().addAll("attente", "confirme", "annule");
//        statutChoiceBox.setValue("attente"); // Valeur par défaut
//        try {
//            chargerEquipements();  // Charger les équipements dans la ComboBox
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Remplir les ComboBox avec les équipements et événements disponibles
//        try {
//            // Remplir la ComboBox des équipements
//            List<Equipement> equipements = serviceReservation.recupererEquipements();
//            for (Equipement e : equipements) {
//                comboBoxEquipement.getItems().add(e.getNom());
//            }
//
//            // Remplir la ComboBox des événements (à adapter si vous avez une table d'événements)
//            List<evenement> evenements = serviceReservation.recupererEvenements();
//            for (evenement ev : evenements) {
//                comboBoxEvenement.getItems().add(ev.getTitre());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Erreur", "Erreur lors de la récupération des équipements et événements.");
//        }
//    }
@FXML
private void initialize() {
    statutChoiceBox.getItems().addAll("attente", "confirme", "annule");
    statutChoiceBox.setValue("attente"); // Valeur par défaut

    try {
        // Remplir la ComboBox des équipements
        List<Equipement> equipements = serviceReservation.recupererEquipements();
        for (Equipement e : equipements) {
            comboBoxEquipement.getItems().add(e.getNom());
        }

        // Remplir la ComboBox des événements
        List<evenement> evenements = serviceReservation.recupererEvenements();
        for (evenement ev : evenements) {
            comboBoxEvenement.getItems().add(ev.getTitre());
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Erreur", "Erreur lors de la récupération des équipements et événements.");
    }
}

    // Méthode pour initialiser l'équipement sélectionné depuis le contrôleur parent
    public void initData(Equipement equipement) {
        this.equipement = equipement;
        comboBoxEquipement.setValue(equipement.getNom()); // Mettre à jour le ComboBox avec l'équipement sélectionné
    }
    // Charger tous les équipements dans la ComboBox
    public void chargerEquipements() throws SQLException {
        List<Equipement> equipements = serviceReservation.LesEquipements();  // Service pour récupérer les équipements
        for (Equipement equipement : equipements) {
            comboBoxEquipement.getItems().add(equipement.getNom());  // Ajouter uniquement le nom dans la ComboBox
        }
    }
    @FXML
    private void ajouterReservation(ActionEvent event) {
        try {
            // Récupérer le titre de l'événement sélectionné
            String titreEvenement = comboBoxEvenement.getValue();
            if (titreEvenement == null || titreEvenement.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner un événement.");
                return;
            }

            // Récupérer l'événement complet (y compris l'ID) à partir du titre
            evenement evenement = serviceReservation.getEvenementParNom(titreEvenement);
            if (evenement == null) {
                showAlert("Erreur", "L'événement sélectionné n'existe pas.");
                return;
            }

            // Récupérer l'équipement sélectionné dans la ComboBox (par son nom ou ID)
            String nomEquipementSelectionne = comboBoxEquipement.getValue();
            if (nomEquipementSelectionne == null || nomEquipementSelectionne.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner un équipement.");
                return;
            }

            Equipement equipement = serviceReservation.getEquipementParNom(nomEquipementSelectionne);
            if (equipement == null) {
                showAlert("Erreur", "L'équipement sélectionné n'existe pas.");
                return;
            }

            // Récupérer le statut de la réservation
            String statutStr = statutChoiceBox.getValue();
            Status_reservation statut;
            try {
                statut = Status_reservation.valueOf(statutStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "Statut de réservation invalide.");
                return;
            }

            // Récupérer la date de réservation
            if (datePickerReservation.getValue() == null) {
                showAlert("Erreur", "Veuillez sélectionner une date de réservation.");
                return;
            }
            String dateReservation = datePickerReservation.getValue().toString();

            // Créer la réservation avec l'équipement et l'événement
            Reservation_Equipement reservation = new Reservation_Equipement(evenement, equipement, dateReservation, statut);

            // Appeler la méthode d'ajout du service
            serviceReservation.ajouter(reservation);

            // Afficher un message de succès
            showAlert("Succès", "Réservation ajoutée avec succès.");
            naviguerVersAfficherReservation();


        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout.");
        }
    }
//    private void naviguerVersAfficherReservation() {
//        try {
//            // Charger le fichier FXML de l'interface AfficherReservation
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
//            Parent root = loader.load();
//
//            // Créer une nouvelle scène
//            Scene scene = new Scene(root);
//
//            // Récupérer la fenêtre actuelle (stage)
//            Stage stage = (Stage) btnAjouter.getScene().getWindow();
//
//            // Changer la scène de la fenêtre
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Erreur", "Impossible de charger l'interface AfficherReservation.");
//        }
//    }
private void naviguerVersAfficherReservation() {
    try {
        // Charger le fichier FXML de l'interface AfficherReservation
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Récupérer la fenêtre actuelle (stage)
        Stage stage = (Stage) btnAjouter.getScene().getWindow();

        // Changer la scène de la fenêtre
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Erreur", "Impossible de charger l'interface AfficherReservation. Détails : " + e.getMessage());
    }
}

//    @FXML
//    private void ajouterReservation(ActionEvent event) {
//        try {
//            // Récupérer l'événement à partir de la ComboBox
//            evenement evenement = new evenement();
//            evenement.setTitre(comboBoxEvenement.getValue());
//
//            // Récupérer l'équipement sélectionné dans la ComboBox (par son nom ou ID)
//            String nomEquipementSelectionne = comboBoxEquipement.getValue();
//            Equipement equipement = serviceReservation.getEquipementParNom(nomEquipementSelectionne);
//
//            if (equipement == null) {
//                showAlert("Erreur", "L'équipement sélectionné n'existe pas.");
//                return;
//            }
//
//            // Récupérer le statut de la réservation
//            String statutStr = statutChoiceBox.getValue();
//            Status_reservation statut;
//            try {
//                statut = Status_reservation.valueOf(statutStr.toUpperCase());
//            } catch (IllegalArgumentException e) {
//                showAlert("Erreur", "Statut de réservation invalide.");
//                return;
//            }
//
//            // Récupérer la date de réservation
//            String dateReservation = datePickerReservation.getValue().toString();
//
//            // Créer la réservation avec l'équipement et l'événement
//            Reservation_Equipement reservation = new Reservation_Equipement(evenement, equipement, dateReservation, statut);
//
//            // Appeler la méthode d'ajout du service
//            serviceReservation.ajouter(reservation);
//
//            // Afficher un message de succès
//            showAlert("Succès", "Réservation ajoutée avec succès.");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Erreur", "Une erreur est survenue lors de l'ajout.");
//        }
//    }

    @FXML
    public void onEquipementSelected() throws SQLException {
        String nomEquipementSelectionne = comboBoxEquipement.getValue();  // Le nom de l'équipement sélectionné

        if (nomEquipementSelectionne != null && !nomEquipementSelectionne.isEmpty()) {
            // Récupérer l'équipement complet à partir du nom sélectionné
            Equipement equipement = serviceReservation.getEquipementParNom(nomEquipementSelectionne);

            if (equipement != null) {
                // Vous avez l'équipement complet, vous pouvez utiliser son ID pour la réservation, mais vous ne l'affichez pas
                System.out.println("Équipement sélectionné : " + equipement.getNom());

                // Utiliser l'équipement sélectionné (ID est utilisé en interne)
                // Pas besoin de l'afficher dans un label
            } else {
                System.out.println("Erreur : Équipement non trouvé.");
                showAlert("Erreur", "L'équipement sélectionné n'a pas été trouvé.");
            }
        } else {
            // Dans le cas où aucun équipement n'est sélectionné
            System.out.println("Erreur : Veuillez sélectionner un équipement.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





}
