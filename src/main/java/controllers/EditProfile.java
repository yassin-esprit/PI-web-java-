package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.User;
import tools.MyDB;
import tools.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class EditProfile {
    Connection cnx = MyDB.getInstance().getConnection();
    @FXML
    private AnchorPane ProfileEdit;

    @FXML
    private TextField email;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField phone;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();

        fname.setText(currentUser.getFirstName());
        lname.setText(currentUser.getLastName());
        email.setText(currentUser.getEmail());
        phone.setText(String.valueOf(currentUser.getPhone()));
    }

    @FXML
    void editprofilebutton(MouseEvent event) {
        String firstName = fname.getText().trim();
        String lastName = lname.getText().trim();
        String emailText = email.getText().trim();
        String phoneText = phone.getText().trim();

        // Validate required fields
        if (firstName.isEmpty() || lastName.isEmpty() || emailText.isEmpty()) {
            showErrorAlert("Error", "First Name, Last Name, and Email are required.");
            return;
        }

        // Validate email format
        if (!isValidEmail(emailText)) {
            showErrorAlert("Error", "Please enter a valid email address.");
            return;
        }

        // Validate phone number
        String phoneNumber = String.valueOf('0');
        if (!phoneText.isEmpty()) {
            try {
                phoneNumber = String.valueOf(Integer.parseInt(phoneText));
            } catch (NumberFormatException e) {
                showErrorAlert("Error", "Phone number must be a valid number.");
                return;
            }
        }

        try {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, phone = ?, email = ? WHERE user_id = ?";
            PreparedStatement ste = cnx.prepareStatement(sql);
            ste.setString(1, firstName);
            ste.setString(2, lastName);
            ste.setString(3, phoneNumber);
            ste.setString(4, emailText);
            ste.setInt(5, UserSession.getInstance().getUser().getUserId());

            int rowsUpdated = ste.executeUpdate();
            if (rowsUpdated > 0) {
                // Update the session user data
                User currentUser = UserSession.getInstance().getUser();
                currentUser.setFirstName(firstName);
                currentUser.setLastName(lastName);
                currentUser.setEmail(emailText);
                currentUser.setPhone(phoneNumber);

                showSuccessAlert("Success", "Profile updated successfully!");
                SceneController.loadProfilePageScene();
            } else {
                showErrorAlert("Error", "Failed to update profile.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while updating the profile.");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Navigation Error", "Unable to return to profile page.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void BackProfilebtn(MouseEvent event) throws IOException {
        SceneController.loadAdminDashScene();
    }
}