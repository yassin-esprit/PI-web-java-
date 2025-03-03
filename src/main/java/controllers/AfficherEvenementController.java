package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Participation;
import models.Rating;
import models.User;
import models.evenement;
import services.ServiceEvenement;
import services.ServiceParticipation;
import services.ServiceRating;
import tools.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfficherEvenementController implements Initializable {


    @FXML private RadioButton rating1, rating2, rating3, rating4, rating5;
    @FXML private TextArea reviewText;
    @FXML private ToggleGroup ratingGroup = new ToggleGroup();
    @FXML private VBox ratingSection; // Add this field with fx:id="ratingSection" on the VBox


    @FXML
    private ListView<evenement> listViewEvenements;
    @FXML
    private VBox ratingsContainer;
    @FXML
    private TextField searchField;
    @FXML
    private Label mail;

    @FXML
    private Label name;

    @FXML
    private Label lastname;


    @FXML
    private Button btnMesParticipations;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();
    private ServiceParticipation serviceParticipation = new ServiceParticipation();
    private ServiceRating serviceRating = new ServiceRating();


    @FXML
    public void logout(MouseEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        session.cleanUserSession(); // Clear the user session
        SceneController.loadLoginScene(); // Redirect to login scene
    }


    @FXML
    private void redirectToMesParticipations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mesParticipations.fxml"));
            Parent root = loader.load();
            btnMesParticipations.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des participations : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleParticipate() {
        evenement selectedEvent = listViewEvenements.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement pour participer.", Alert.AlertType.ERROR);
            return;
        }

        // Get the current user from the session
        UserSession userSession = UserSession.getInstance();
        if (userSession.getUser() == null) {
            showAlert("Erreur", "Aucun utilisateur connecté.", Alert.AlertType.ERROR);
            return;
        }

        int userId = userSession.getUser().getUserId(); // Get the current user's ID
        int eventId = selectedEvent.getId();

        Participation participation = new Participation(eventId, userId);

        try {
            serviceParticipation.ajouterParticipation(participation);
            showAlert("Succès", "Vous avez participé à l'événement avec succès !", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEvenements();
        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();
        if (currentUser != null) {
            name.setText(currentUser.getFirstName());
            lastname.setText(currentUser.getLastName());
            mail.setText(currentUser.getEmail());
        } else {
            System.err.println("No user logged in during initialization!");
        }

        // Bind radio buttons to the toggle group
        if (rating1 != null) rating1.setToggleGroup(ratingGroup);
        if (rating2 != null) rating2.setToggleGroup(ratingGroup);
        if (rating3 != null) rating3.setToggleGroup(ratingGroup);
        if (rating4 != null) rating4.setToggleGroup(ratingGroup);
        if (rating5 != null) rating5.setToggleGroup(ratingGroup);

        // Load ratings when an event is selected
        listViewEvenements.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadRatingsForEvent(newVal.getId());
            }
        });
        if (ratingSection != null) {
            ratingSection.setVisible(false);
        }

        // Show rating section when an event is selected
        listViewEvenements.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadRatingsForEvent(newVal.getId());
                if (ratingSection != null) {
                    ratingSection.setVisible(true);
                }
            } else {
                if (ratingSection != null) {
                    ratingSection.setVisible(false);
                }
            }
        });

    }

    @FXML
    private void submitReview() {
        evenement selectedEvent = listViewEvenements.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement pour noter.", Alert.AlertType.ERROR);
            return;
        }

        int rating = 0;
        if (rating1.isSelected()) rating = 1;
        else if (rating2.isSelected()) rating = 2;
        else if (rating3.isSelected()) rating = 3;
        else if (rating4.isSelected()) rating = 4;
        else if (rating5.isSelected()) rating = 5;

        if (rating == 0) {
            showAlert("Erreur", "Veuillez sélectionner une note.", Alert.AlertType.ERROR);
            return;
        }

        String review = reviewText.getText();
        int userId = UserSession.getInstance().getUser().getUserId();
        Rating newRating = new Rating(0, selectedEvent.getId(), userId, rating, review, null);

        try {
            serviceRating.addRating(newRating);
            showAlert("Succès", "Merci pour votre avis !", Alert.AlertType.INFORMATION);
            loadRatingsForEvent(selectedEvent.getId()); // Refresh ratings
            reviewText.clear(); // Clear the review text
            ratingGroup.selectToggle(null); // Reset radio buttons
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la soumission de l'avis: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void loadRatingsForEvent(int eventId) {
        ratingsContainer.getChildren().clear();
        try {
            List<Rating> ratings = serviceRating.getRatingsForEvent(eventId);
            if (ratings.isEmpty()) {
                ratingsContainer.getChildren().add(new Label("Aucun avis pour cet événement."));
            } else {
                for (Rating rating : ratings) {
                    Label reviewLabel = new Label(
                            "Note: " + rating.getRating() + "/5\n" +
                                    "Avis: " + rating.getReview() + "\n" +
                                    "Utilisateur: " + rating.getUserId() + "\n" +
                                    "Date: " + rating.getCreatedAt()
                    );
                    reviewLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                    ratingsContainer.getChildren().add(reviewLabel);
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des avis: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

//    private void loadEvenements() {
//        try {
//            List<evenement> evenements = serviceEvenement.recuperer();
//            ObservableList<evenement> observableList = FXCollections.observableArrayList(evenements);
//            listViewEvenements.setItems(observableList);
//
//            listViewEvenements.setCellFactory(listView -> new ListCell<evenement>() {
//                @Override
//                protected void updateItem(evenement event, boolean empty) {
//                    super.updateItem(event, empty);
//                    if (empty || event == null) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        setStyle("-fx-padding: 15px; -fx-background-color: #f9f9f9; "
//                                + "-fx-border-color: #ddd; -fx-border-radius: 10px; "
//                                + "-fx-font-size: 14px; -fx-font-weight: bold;");
//                        setText("\uD83D\uDCC5 " + event.getDate() + "\n"
//                                + "\uD83C\uDF1F Titre: " + event.getTitre() + "\n"
//                                + "📌 Type: " + event.getType() + "\n"
//                                + "📖 Description: " + event.getDescription() + "\n"
//                                + "📍 Lieu: " + event.getLieu() + "\n"
//                                + "👥 Places: " + event.getNbrplaces() + "\n"
//                                + "🤝 Sponsor: " + event.getSponsor());
//                    }
//                }
//            });
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Erreur", "Une erreur s'est produite lors du chargement des événements.", Alert.AlertType.ERROR);
//        }
//    }

    private void loadEvenements() {
        try {
            List<evenement> evenements = serviceEvenement.recuperer();
            ObservableList<evenement> observableList = FXCollections.observableArrayList(evenements);
            listViewEvenements.setItems(observableList);

            listViewEvenements.setCellFactory(listView -> new ListCell<evenement>() {
                @Override
                protected void updateItem(evenement event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setStyle("-fx-padding: 15px; -fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 10px; -fx-font-size: 14px; -fx-font-weight: bold;");
                        setText("\uD83D\uDCC5 " + event.getDate() + "\n"
                                + "\uD83C\uDF1F Titre: " + event.getTitre() + "\n"
                                + "📌 Type: " + event.getType() + "\n"
                                + "📖 Description: " + event.getDescription() + "\n"
                                + "📍 Lieu: " + event.getLieu() + "\n"
                                + "👥 Places: " + event.getNbrplaces() + "\n"
                                + "🤝 Sponsor: " + event.getSponsor());
                    }
                }
            });
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des événements: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    void deleteEvenement(ActionEvent event) {
        evenement selectedEvenement = listViewEvenements.getSelectionModel().getSelectedItem();

        if (selectedEvenement == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un événement à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceEvenement.supprimer(selectedEvenement.getId());
                listViewEvenements.getItems().remove(selectedEvenement);
                showAlert("Succès", "Événement supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de l'événement.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void modifyEvenement(ActionEvent event) {
        evenement selectedEvenement = listViewEvenements.getSelectionModel().getSelectedItem();

        if (selectedEvenement == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un événement à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierevenement.fxml"));
            Parent root = loader.load();
            ModifierEvenementController controller = loader.getController();
            controller.initData(selectedEvenement);
            listViewEvenements.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement de la page de modification.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterevenement.fxml"));
            Parent root = loader.load();
            listViewEvenements.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du retour : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void editpage(MouseEvent mouseEvent) throws IOException {
        SceneController.loadProfileEditScene();

    }

    @FXML
    public void showStatistics() {
        try {
            // Load the statistics FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statistics.fxml"));
            Parent root = loader.load();

            // Create a new scene and stage for the statistics page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques des Événements");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @FXML
//    private void viewDetails() {
//        // Example: Show an alert with event details
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Event Details");
//        alert.setHeaderText("Details for the Event");
//
//        // Here, you can dynamically set the content of the alert based on the selected event
//        alert.setContentText("Event Title: Example Event\nEvent Type: Conference\nDate: March 5, 2025\nLocation: New York");
//
//        alert.showAndWait();
//    }

    @FXML
    private void handleSort(ActionEvent event) {
        // Show a dialog to select the event type
        ChoiceDialog<String> dialog = new ChoiceDialog<>("conference", "conference", "fete", "formation");
        dialog.setTitle("Trier par Type");
        dialog.setHeaderText("Sélectionnez un type d'événement");
        dialog.setContentText("Type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(type -> {
            try {
                List<evenement> allEvents = serviceEvenement.recuperer();
                ObservableList<evenement> sortedEvents = FXCollections.observableArrayList(
                        allEvents.stream()
                                .filter(e -> e.getType().name().equalsIgnoreCase(type))
                                .collect(Collectors.toList())
                );
                listViewEvenements.setItems(sortedEvents);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors du tri.", Alert.AlertType.ERROR);
            }
        });
    }


    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();
        try {
            List<evenement> allEvents = serviceEvenement.recuperer();
            ObservableList<evenement> filteredEvents = FXCollections.observableArrayList(
                    allEvents.stream()
                            .filter(e -> e.getTitre().toLowerCase().contains(searchText) ||
                                    e.getDescription().toLowerCase().contains(searchText) ||
                                    e.getLieu().toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            listViewEvenements.setItems(filteredEvents);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la recherche.", Alert.AlertType.ERROR);
        }
    }



//    private void loadRatings() {
//        ratingsContainer.getChildren().clear();
//        try {
//            List<Rating> ratings = serviceRating.getRatingsForEvent();
//            for (Rating rating : ratings) {
//                Label reviewLabel = new Label(
//                        "Rating: " + rating.getRating() + "/5\n" +
//                                "Review: " + rating.getReview() + "\n" +
//                                "By User: " + rating.getUserId() + "\n" +
//                                "On: " + rating.getCreatedAt()
//                );
//                reviewLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
//                ratingsContainer.getChildren().add(reviewLabel);
//            }
//        } catch (SQLException e) {
//            showAlert("Error", "Failed to load reviews: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
}
