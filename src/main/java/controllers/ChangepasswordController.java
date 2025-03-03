package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.User;
import tools.MyDB;
import tools.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangepasswordController {

    @FXML
    private TextField cnewpass;
    @FXML
    private TextField newpass;
    @FXML
    private TextField old;

    @FXML
    void passrecovery(MouseEvent event) {
        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();

        String oldPassword = old.getText();
        String newPassword = newpass.getText();
        String confirmNewPassword = cnewpass.getText();

        // Validation checks
        if (!validateInputs(currentUser, oldPassword, newPassword, confirmNewPassword)) {
            return;
        }

        // Update password in database
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection cnx = MyDB.getInstance().getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, currentUser.getUserId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                updateUserSession(session, currentUser, newPassword);
                showAlert("Success", "Password updated successfully", Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Error", "Failed to update password", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private boolean validateInputs(User user, String oldPass, String newPass, String confirmNewPass) {
        if (!oldPass.equals(user.getPassword())) {
            showAlert("Error", "Old password is incorrect", Alert.AlertType.ERROR);
            return false;
        }
        if (!newPass.equals(confirmNewPass)) {
            showAlert("Error", "New passwords do not match", Alert.AlertType.ERROR);
            return false;
        }
        if (newPass.isBlank()) {
            showAlert("Error", "New password cannot be empty", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void updateUserSession(UserSession session, User user, String newPassword) {
        user.setPassword(newPassword);

    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        old.clear();
        newpass.clear();
        cnewpass.clear();
    }

    private void handleDatabaseError(SQLException e) {
        System.err.println("Database error:");
        e.printStackTrace();
        showAlert("Database Error",
                "Error updating password: " + e.getMessage(),
                Alert.AlertType.ERROR);
    }

    @FXML
    public void backtologinpage(MouseEvent mouseEvent) throws IOException {
        SceneController.loadSettingPageScene();
    }
}