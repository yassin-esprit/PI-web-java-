package Services;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

public class InputValidation {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,63}$";

        if (email == null || email.trim().isEmpty()) {
            showError("Email cannot be empty");
            return false;
        }

        if (!email.matches(emailRegex)) {
            showError("Invalid email format (example: user@domain.com)");
            return false;
        }

        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            showError("Password cannot be empty");
            return false;
        }

        List<String> errors = new ArrayList<>();

        if (password.length() < 8) {
            errors.add("at least 8 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("one number");
        }
        if (!password.matches(".*[@#$%^&+=].*")) {
            errors.add("one special character (@#$%^&+=)");
        }

        if (!errors.isEmpty()) {
            showError("Password requires: " + String.join(", ", errors));
            return false;
        }

        return true;
    }

    public static boolean isValidName(String name) {
        String nameRegex = "^[a-zA-Z\\s'-]{2,50}$";

        if (name == null || name.trim().isEmpty()) {
            showError("Name cannot be empty");
            return false;
        }

        String cleanedName = name.trim();
        if (!cleanedName.matches(nameRegex)) {
            showError("Only letters, spaces, hyphens and apostrophes allowed (2-50 chars)");
            return false;
        }

        return true;
    }

    private static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
