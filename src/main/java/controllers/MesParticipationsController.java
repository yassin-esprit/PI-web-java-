package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.evenement;
import services.ServiceEvenement;
import services.ServiceParticipation;
import tools.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MesParticipationsController {

    @FXML
    private ListView<evenement> listViewMesParticipations;

    private ServiceParticipation serviceParticipation = new ServiceParticipation();
    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    private void initialize() {
        loadParticipatingEvents();
        setupListView();
    }

    private void loadParticipatingEvents() {
        UserSession userSession = UserSession.getInstance();
        if (userSession.getUser() == null) {
            return; // No user logged in
        }

        int userId = userSession.getUser().getUserId();

        try {
            // Fetch event IDs the user is participating in
            List<Integer> eventIds = serviceParticipation.getEventsByUserId(userId);

            // Fetch event details for each event ID
            for (int eventId : eventIds) {
                evenement event = serviceEvenement.getEventById(eventId);
                if (event != null) {
                    listViewMesParticipations.getItems().add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupListView() {
        // Set a custom cell factory to style and organize the ListView items
        listViewMesParticipations.setCellFactory(listView -> new ListCell<evenement>() {
            @Override
            protected void updateItem(evenement event, boolean empty) {
                super.updateItem(event, empty);

                if (empty || event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Customize the display of each event
                    setStyle("-fx-padding: 15px; "
                            + "-fx-background-color: #f9f9f9; "
                            + "-fx-border-color: #ddd; "
                            + "-fx-border-radius: 10px; "
                            + "-fx-font-size: 14px; "
                            + "-fx-font-weight: bold;");

                    setText("\uD83D\uDCC5 Date: " + event.getDate() + "\n"
                            + "\uD83C\uDF1F Titre: " + event.getTitre() + "\n"
                            + "📌 Type: " + event.getType() + "\n"
                            + "📖 Description: " + event.getDescription() + "\n"
                            + "📍 Lieu: " + event.getLieu() + "\n"
                            + "👥 Places: " + event.getNbrplaces() + "\n"
                            + "🤝 Sponsor: " + event.getSponsor());
                }
            }
        });
    }

    @FXML
    private void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherevenement.fxml"));
            Parent root = loader.load();
            listViewMesParticipations.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}