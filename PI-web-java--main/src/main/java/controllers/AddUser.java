package controllers;

import models.User;
import models.User.Role;
import services.InputValidation;
import services.UserServices;
import tools.MyDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddUser {

    private UserServices user = new UserServices();
    private Connection cnx = MyDB.getInstance().getConnection();

    @FXML
    private TextField fname, lname, mail, phn;
    @FXML
    private PasswordField pass;
    @FXML
    private ComboBox<Role> roleComboBox; // Updated to use Enum
    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        // Populate ComboBox with Role enum values
        roleComboBox.getItems().setAll(Role.values());
    }

    @FXML
    public void singup(ActionEvent event) {
        try {
            // Get input values
            String firstName = fname.getText();
            String lastName = lname.getText();
            String email = mail.getText();
            String phone = phn.getText();
            String password = pass.getText();
            Role selectedRole = roleComboBox.getValue();

            // Validate input
            if (selectedRole == null) {
                showError("Please select a role.");
                return;
            }

            String checkEmailSql = "SELECT COUNT(*) FROM user WHERE email = ?";
            try (PreparedStatement checkStmt = cnx.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    showError("Email already exists!");
                    return;
                }
            }

            // Validate phone number format
            String phoneNumber;
            try {
                phoneNumber = String.valueOf(Integer.parseInt(phone));
            } catch (NumberFormatException e) {
                showError("Invalid phone number format");
                return;
            }

            // Input validations
            if (!InputValidation.isValidName(firstName)) {
                showError("First name is invalid. Only letters, spaces, hyphens, and apostrophes allowed.");
                return;
            }

            if (!InputValidation.isValidName(lastName)) {
                showError("Last name is invalid. Only letters, spaces, hyphens, and apostrophes allowed.");
                return;
            }

            if (!InputValidation.isValidEmail(email)) {
                showError("Invalid email format. Please provide a valid email.");
                return;
            }

            if (!InputValidation.isValidPassword(password)) {
                showError("Password must be at least 8 characters long, with one uppercase letter, one lowercase letter, one number, and one special character.");
                return;
            }

            // Proceed to add user
            user.add(new User(
                    firstName,
                    lastName,
                    phoneNumber,
                    email,
                    password,
                    selectedRole // Using Enum
            ));

            // Optionally, redirect to the dashboard or a success page
            showSuccess("User created successfully!");

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Show error messages in a pop-up
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show success message in a pop-up
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void loginlink(ActionEvent event) throws IOException {
        // Navigate to login page
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
