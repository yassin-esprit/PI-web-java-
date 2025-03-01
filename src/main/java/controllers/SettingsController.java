package controllers;

import models.User;
import tools.MyDB;
import tools.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private Label edit;

    @FXML
    private Label edit1;

    @FXML
    void Changepass(MouseEvent event) throws IOException {
        SceneController.loadpasswordPageScene();

    }
    @FXML


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void deleteAccount(MouseEvent event) {
        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();

        // Confirm deletion with the user
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Account Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmationAlert.setContentText("This action cannot be undone. All your data will be permanently deleted.");

        // Show confirmation dialog and wait for user response
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            String sql = "DELETE FROM users WHERE user_id = ?";

            try (Connection cnx = MyDB.getInstance().getConnection();
                 PreparedStatement stmt = cnx.prepareStatement(sql)) {

                stmt.setInt(1, currentUser.getUserId());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    // Account deleted successfully
                    showAlert("Success", "Your account has been deleted successfully.", Alert.AlertType.INFORMATION);

                    // Clear the session and redirect to login page
                    session.cleanUserSession();
                    SceneController.loadLoginScene();
                } else {
                    showAlert("Error", "Failed to delete account. Please try again.", Alert.AlertType.ERROR);
                }

            } catch (IOException e) {
                showAlert("Error", "Failed to redirect to login page.", Alert.AlertType.ERROR);
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void gotoprofile(MouseEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        System.out.println("##################");

        System.out.println(session.getUser().getRole());
        System.out.println("##################");

        if ("Admin".equals(session.getUser().getRole())) {
            SceneController.loadAdminDashScene();
        } else {
            SceneController.loadProfilePageScene();
        }
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        session.cleanUserSession();
        SceneController.loadLoginScene();
    }

}
