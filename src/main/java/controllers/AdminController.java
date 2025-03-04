package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import models.User;
import models.User.Role;
import tools.MyDB;
import tools.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {
    @FXML
    private TableColumn<User, Void> action;
    @FXML
    private TableView<User> usertabel;
    @FXML
    private TextField searchField;

    @FXML
    private Label mail;

    @FXML
    private Label name;

    @FXML
    private Label lastname;

    @FXML
    private TableColumn<User, String> fname;
    @FXML
    private TableColumn<User, String> lname;
    @FXML
    private TableColumn<User, String> phone;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, String> role;

    private Connection cnx = MyDB.getInstance().getConnection();
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();

        // Display current user info
        name.setText(currentUser.getFirstName());
        lastname.setText(currentUser.getLastName());
        mail.setText(currentUser.getEmail());

        setupTableColumns();
        loadUsersFromDatabase();

        FilteredList<User> filteredUsers = new FilteredList<>(usersList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || user.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || user.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(user.getPhone()).contains(lowerCaseFilter)
                        || user.getRole().name().toLowerCase().contains(lowerCaseFilter);
            });
        });
        usertabel.setItems(filteredUsers);
    }

    private void setupTableColumns() {
        fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        action.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    GridPane pane = new GridPane();
                    pane.setHgap(10);
                    pane.add(editButton, 0, 0);
                    pane.add(deleteButton, 1, 0);
                    setGraphic(pane);
                }
            }
        });
    }


    private void handleEditUser(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit User Details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField(user.getFirstName());
        TextField lastNameField = new TextField(user.getLastName());
        TextField phoneField = new TextField(String.valueOf(user.getPhone()));
        TextField emailField = new TextField(user.getEmail());
        ComboBox<Role> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(Role.ADMIN, Role.OWNER, Role.PARTICIPANT);  // Using Enum here
        roleComboBox.setValue(user.getRole());

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Role:"), 0, 4);
        grid.add(roleComboBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String phone = String.valueOf(Integer.parseInt(phoneField.getText()));
                    return new User(
                            firstNameField.getText(),
                            lastNameField.getText(),
                            phone,
                            emailField.getText(),
                            roleComboBox.getValue()  // Pass Role enum directly
                    );
                } catch (NumberFormatException e) {
                    showAlert("Invalid Phone", "Please enter a valid numeric phone number.");
                    return null;
                }
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(updatedUser -> {
            String query = "UPDATE user SET firstname=?, lastname=?, phone=?, email=?, role=? WHERE email=?";
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, updatedUser.getFirstName());
                pst.setString(2, updatedUser.getLastName());
                pst.setString(3, updatedUser.getPhone());
                pst.setString(4, updatedUser.getEmail());
                pst.setString(5, updatedUser.getRole().name());  // Save role as string (Enum's name)
                pst.setString(6, user.getEmail());
                pst.executeUpdate();
                loadUsersFromDatabase();
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to update user: " + e.getMessage());
            }
        });
    }


    private void handleDeleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("User: " + user.getFirstName() + " " + user.getLastName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String query = "DELETE FROM user WHERE email = ?";
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, user.getEmail());
                pst.executeUpdate();
                usersList.remove(user);  // Remove from observable list
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to delete user: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUsersFromDatabase() {
        usersList.clear();
        String query = "SELECT firstname, lastname, phone, email, role FROM user";
        try (PreparedStatement pst = cnx.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String roleStr = rs.getString("role"); // Get the role as a String
                Role role = Role.valueOf(roleStr.toUpperCase()); // Convert the String to a Role enum

                usersList.add(new User(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        role // Pass the Role enum
                ));
            }
        } catch (SQLException | IllegalArgumentException e) {
            showAlert("Database Error", "Error loading users: " + e.getMessage());
        }
    }

    @FXML
    void adresspage(MouseEvent event) throws IOException {
        SceneController.loadAdressScene();
    }

    @FXML
    void editadress(MouseEvent event) throws IOException {
        SceneController.loadProfileEditScene();
    }

    @FXML
    void editpage(MouseEvent event) throws IOException {
        SceneController.loadProfileEditScene();

    }

    @FXML
    void gotosetting(MouseEvent event) throws IOException {
        SceneController.loadSettingPageScene();
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        UserSession session = UserSession.getInstance();
        session.cleanUserSession();
        SceneController.loadLoginScene();
    }
    public void navEvenementlist(MouseEvent mouseEvent) throws IOException {
        SceneController.loadlistev();
    }
    public void navPublist(MouseEvent mouseEvent) throws IOException {
        SceneController.loadlistpub();
    }

    public void navCamplist(MouseEvent mouseEvent) throws IOException {
        SceneController.loadCamplist();
    }
}
