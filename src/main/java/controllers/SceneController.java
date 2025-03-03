    package controllers;

    import javafx.application.Platform;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    import java.io.IOException;
    import java.util.Objects;

    public class SceneController {

        private static Stage primaryStage ;

        // Initialize primaryStage, call this first in your main class
        public static void setPrimaryStage(Stage stage) {
            primaryStage = stage;
        }

        // General method to load a scene with its corresponding title
        private static void loadScene(String fxmlFile, String title) throws IOException {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxmlFile)));
            Platform.runLater(root::requestFocus);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
        }

        // Method to load the SignUp scene
        public static void loadSignupScene() throws IOException {
            loadScene("/add.fxml", "Sign Up");
        }

        // Method to load the Login scene
        public static void loadLoginScene() throws IOException {
            loadScene("/login.fxml", "Login");
        }

        // Method to load the Profile Page
        public static void loadProfilePageScene() throws IOException {
            loadScene("/afficherevenement.fxml", "Profile Page");
        }

        // Method to load the Change Password scene
        public static void loadpasswordPageScene() throws IOException {
            loadScene("/ChangePassword.fxml", "Change Password");
        }

        // Method to load the Settings scene
        public static void loadSettingPageScene() throws IOException {
            loadScene("/Settings.fxml", "Settings");
        }

        // Method to load the Address scene
        public static void loadAdressScene() throws IOException {
            loadScene("/adress.fxml", "Address");
        }

        // Method to load the Profile Edit scene
        public static void loadProfileEditScene() throws IOException {
            loadScene("/EditProfile.fxml", "Edit Profile");
        }

        // Method to load the Address Signup scene
        public static void loadAddressSignupScene() throws IOException {
            loadScene("/AdressSingup.fxml", "Address Signup");
        }
        public static void loadlistev() throws IOException {
            loadScene("/evenementdash.fxml", "list Evenements");
        }

        // Method to load the Forgot Password scene
        public static void loadForgotPasswordScene() throws IOException {
            loadScene("/ForgotPassword.fxml", "Forgot Password");
        }

        // Method to load the Admin Dashboard scene
        public static void loadAdminDashScene() throws IOException {
            loadScene("/AdminDash.fxml", "Admin Dashboard");
        }

        public static void loadlisteq() throws IOException {
            loadScene("/AfficherEquipement.fxml", "list Equipements");
        }

        public static void loadlistRes() throws IOException {
            loadScene("/AfficherReservation.fxml", "list Reservations");
        }


    }
