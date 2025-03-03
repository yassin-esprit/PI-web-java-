package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.User;
import tools.MyDB;
import tools.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button Login_button;

    @FXML
    private TextField email;

    @FXML
    private Hyperlink forgotpass;

    @FXML
    private TextField password;

    @FXML
    private CheckBox remember;

    UserSession session = UserSession.getInstance();
    User currentUser = session.getUser();

    @FXML
    void Remember_pass(ActionEvent event) {
        // Handle remember password logic here
    }

    @FXML
    void forgotpass(ActionEvent event) {
        // Handle forgot password logic here
    }

    @FXML
    void login(ActionEvent event) throws SQLException, IOException {
        if (validateLogin(email.getText(), password.getText())) {
            System.out.println("Login successful");

            // Check user role and load the corresponding scene
            User.Role role = session.getUser().getRole();

            if (role.equals(User.Role.PARTICIPANT)) {
                SceneController.loadProfilePageScene();
            } else if (role.equals(User.Role.ADMIN)) {
                SceneController.loadAdminDashScene();
            } else if (role.equals(User.Role.OWNER)) {
//                SceneController.loadOwnerDashScene();
            }

        } else {
            showAlert("Error", "Invalid email or password");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void forgotpasspage(MouseEvent event) throws IOException {
        SceneController.loadForgotPasswordScene();
    }

    @FXML
    private void loginlink() throws IOException {
        SceneController.loadSignupScene();
    }

    private boolean validateLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
        Connection cnx = MyDB.getInstance().getConnection();
        PreparedStatement ste = cnx.prepareStatement(sql);

        ste.setString(1, email);
        ste.setString(2, password);
        ResultSet rs = ste.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("userid");
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String phone = String.valueOf(rs.getInt("phone"));
            String userEmail = rs.getString("email");
            String userPassword = rs.getString("password");
            String roleStr = rs.getString("role"); // Get role as a String
            User.Role role = User.Role.valueOf(roleStr.toUpperCase()); // Convert the String to a Role enum

            // Set the user session with the Role enum
            UserSession session = UserSession.getInstance();
            session.setUser(userId, firstName, lastName, phone, userEmail, userPassword, role);

            return true;
        }

        return false;
    }

    @FXML
    void singup(ActionEvent event) throws IOException {
        SceneController.loadSignupScene();
    }
}
