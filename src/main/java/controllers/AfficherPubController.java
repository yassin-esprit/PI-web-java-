package controllers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;
import models.publicites;
import models.statut_pub;
import services.ServicePublicite;
import tools.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherPubController {
    @FXML
    private ListView<publicites> listView;
    @FXML
    private Button idModifier;
    @FXML
    private Button idSupprimer;
    @FXML
    private Button idReserver;
    @FXML
    private Button idStatistiques;

    @FXML
    private Label mail;
    @FXML
    private Label name;
    @FXML
    private Label lastname;


    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> statusFilter;

    private ObservableList<publicites> publicitesList = FXCollections.observableArrayList();
    private ServicePublicite servicePublicites = new ServicePublicite();
    private Map<publicites, Integer> likesMap = new HashMap<>();
    private Map<publicites, Integer> dislikesMap = new HashMap<>();
    private void showPopUpAd() {
        try {
            // Load the pop-up ad FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopUpAd.fxml"));
            Parent root = loader.load();

            // Create a new stage for the pop-up
            Stage popUpStage = new Stage();
            popUpStage.initStyle(StageStyle.UTILITY); // Use a simple window style
            popUpStage.setTitle("Advertisement");
            popUpStage.setScene(new Scene(root));

            // Show the pop-up
            popUpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize() {
        showPopUpAd();

        UserSession session = UserSession.getInstance();
        User currentUser = session.getUser();
        if (currentUser != null) {
            name.setText(currentUser.getFirstName());
            lastname.setText(currentUser.getLastName());
            mail.setText(currentUser.getEmail());
        } else {
            System.err.println("No user logged in during initialization!");
        };
        // Load CSS if needed
        listView.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        statusFilter.getItems().addAll("Tous", "active", "inactif");
        statusFilter.setValue("Tous");

        // Set custom cell factory with all details and styled buttons
        listView.setCellFactory(param -> new ListCell<publicites>() {
            private final HBox hbox = new HBox();
            private final Label detailsLabel = new Label();
            private final Button likeButton = new Button("Like");
            private final Button dislikeButton = new Button("Dislike");
            private final Label likesCount = new Label();
            private final Label dislikesCount = new Label();

            {
                // Style the buttons
                likeButton.setStyle(
                        "-fx-background-color: #2ecc71; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 12px; " +
                                "-fx-padding: 5px 10px; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-background-radius: 5px; " +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                );

                dislikeButton.setStyle(
                        "-fx-background-color: #e74c3c; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 12px; " +
                                "-fx-padding: 5px 10px; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-background-radius: 5px; " +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                );

                // Style the count labels
                likesCount.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 12px; -fx-padding: 0 5px;");
                dislikesCount.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px; -fx-padding: 0 5px;");

                hbox.getChildren().addAll(detailsLabel, likeButton, likesCount, dislikeButton, dislikesCount);
                hbox.setSpacing(10);

                likeButton.setOnAction(event -> {
                    publicites pub = getItem();
                    if (pub != null) {
                        handleLike(pub);
                        updateCounts(pub);
                    }
                });

                dislikeButton.setOnAction(event -> {
                    publicites pub = getItem();
                    if (pub != null) {
                        handleDislike(pub);
                        updateCounts(pub);
                    }
                });
            }

            private void updateCounts(publicites pub) {
                likesCount.setText("(" + likesMap.getOrDefault(pub, 0) + ")");
                dislikesCount.setText("(" + dislikesMap.getOrDefault(pub, 0) + ")");
            }

            @Override
            protected void updateItem(publicites item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Display all details from the publicites class
                    String details = String.format(
                            "ID: %d\nTitre: %s\nType: %s\nContenu: %s\nEmplacement: %s\nStatut: %s",
                            item.getId_pub(),
                            item.getTitre_pub(),
                            item.getType_pub() != null ? item.getType_pub().toString() : "N/A",
                            item.getContenu_pub(),
                            item.getEmplacement_pub(),
                            item.getStatut_pub() != null ? item.getStatut_pub().toString() : "N/A"
                    );
                    detailsLabel.setText(details);
                    updateCounts(item);
                    setGraphic(hbox);
                }
            }
        });

        updatePublicitesList();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterPublicites());
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> filterPublicites());
    }



    @FXML
    void openMap(ActionEvent event) {
        publicites selectedPub = listView.getSelectionModel().getSelectedItem();
        if (selectedPub == null) {
            showAlert("Aucune publicité sélectionnée", "Veuillez sélectionner une publicité pour afficher la carte.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // URL-encode the location
            String location = URLEncoder.encode(selectedPub.getEmplacement_pub(), StandardCharsets.UTF_8.toString());
            String mapUrl = "https://www.google.com/maps?q=" + location;

            // Create a new stage for the map
            Stage mapStage = new Stage();
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(mapUrl);

            VBox vbox = new VBox(webView);
            Scene scene = new Scene(vbox, 800, 600);
            mapStage.setScene(scene);
            mapStage.setTitle("Map: " + selectedPub.getTitre_pub());
            mapStage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la carte.", Alert.AlertType.ERROR);
        }
    }



    private void handleLike(publicites pub) {
        likesMap.put(pub, likesMap.getOrDefault(pub, 0) + 1);
        System.out.println("Liked: " + pub.getTitre_pub() + " | Likes: " + likesMap.get(pub));
        listView.refresh();
    }

    private void handleDislike(publicites pub) {
        dislikesMap.put(pub, dislikesMap.getOrDefault(pub, 0) + 1);
        System.out.println("Disliked: " + pub.getTitre_pub() + " | Dislikes: " + dislikesMap.get(pub));
        listView.refresh();
    }

    public void updatePublicitesList() {
        try {
            List<publicites> list = servicePublicites.recuperer();
            publicitesList.setAll(list);
            filterPublicites();
        } catch (SQLException e) {
            showAlert("Erreur de base de données", "Erreur lors de la récupération des publicités.", Alert.AlertType.ERROR);
        }
    }

    private void filterPublicites() {
        String searchText = searchField.getText().toLowerCase();
        String selectedStatus = statusFilter.getValue();

        List<publicites> filteredList = publicitesList.stream()
                .filter(pub -> pub.getTitre_pub().toLowerCase().contains(searchText))
                .filter(pub -> {
                    if (selectedStatus.equals("Tous")) {
                        return true;
                    }
                    Map<String, statut_pub> statusMap = new HashMap<>();
                    statusMap.put("active", statut_pub.active);
                    statusMap.put("inactif", statut_pub.inactif);
                    statut_pub statut = statusMap.get(selectedStatus);
                    return statut != null && pub.getStatut_pub() == statut;
                })
                .collect(Collectors.toList());

        listView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    void supprimer(ActionEvent event) {
        publicites selectedPub = listView.getSelectionModel().getSelectedItem();
        if (selectedPub != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette publicité ?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        servicePublicites.supprimer(selectedPub.getId_pub());
                        updatePublicitesList();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Aucune publicité sélectionnée", "Veuillez sélectionner une publicité à supprimer.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        publicites selectedPub = listView.getSelectionModel().getSelectedItem();
        if (selectedPub != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierpub.fxml"));
                Parent root = loader.load();
                ModifierPubController controller = loader.getController();
                controller.initData(selectedPub);
                Stage stage = (Stage) idModifier.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune publicité sélectionnée", "Veuillez sélectionner une publicité à modifier.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void gotoCampagnePub(ActionEvent event) {
        publicites selectedPub = listView.getSelectionModel().getSelectedItem();
        if (selectedPub == null) {
            showAlert("Aucune publicité sélectionnée", "Veuillez sélectionner une publicité.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCamp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Campagne Publicitaire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherStatistiques(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiquesPub.fxml"));
            Parent root = loader.load();
            StatistiquesPubController statsController = loader.getController();
            statsController.initData(likesMap, dislikesMap);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques des Publicités");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture des statistiques.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public void navevnements(ActionEvent actionEvent) throws IOException {
        SceneController.loadevPart();

    }
}