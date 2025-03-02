package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Rating;
import services.ServiceRating;
import tools.UserSession;

import java.sql.SQLException;

public class EventDetailsController {

    @FXML
    private RadioButton rating1, rating2, rating3, rating4, rating5;

    @FXML
    private TextArea reviewText;

    private ServiceRating serviceRating = new ServiceRating();
    private int eventId; // Set this when loading the event details

    @FXML
    private void submitReview() {
        int rating = 0;
        if (rating1.isSelected()) rating = 1;
        else if (rating2.isSelected()) rating = 2;
        else if (rating3.isSelected()) rating = 3;
        else if (rating4.isSelected()) rating = 4;
        else if (rating5.isSelected()) rating = 5;

        if (rating == 0) {
            showAlert("Error", "Please select a rating.", Alert.AlertType.ERROR);
            return;
        }

        String review = reviewText.getText();
        int userId = UserSession.getInstance().getUser().getUserId();

        Rating newRating = new Rating(0, eventId, userId, rating, review, null);

        try {
            serviceRating.addRating(newRating);
            showAlert("Success", "Thank you for your review!", Alert.AlertType.INFORMATION);
            // Optionally, refresh the ratings display
        } catch (SQLException e) {
            showAlert("Error", "Failed to submit review: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}