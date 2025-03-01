package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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