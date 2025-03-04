package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Don;
import models.Evenement;
import models.Marchandise;
import services.MarchandiseService;
import services.ServiceDon;
import services.ServiceEvenement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;

public class MarchandiseController implements Initializable {
    @FXML private VBox mainContent;

    private ServiceDon serviceDon = new ServiceDon();
    private ServiceEvenement serviceEvenement = new ServiceEvenement();
    private MarchandiseService marchandiseService = new MarchandiseService();
    private ObservableList<Don> donList = FXCollections.observableArrayList();
    private ObservableList<Marchandise> marchandiseList = FXCollections.observableArrayList();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showDonList();
    }

    @FXML
    private void showAddMarchandiseForm() {
        mainContent.getChildren().clear();

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(javafx.geometry.Pos.CENTER);

        Label nomLabel = new Label("Nom");
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField nomField = new TextField();
        nomField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Label imageLabel = new Label("Image");
        imageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField imageField = new TextField();
        imageField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        imageField.setEditable(false);

        Button browseButton = new Button("Parcourir...");
        browseButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8; -fx-border-radius: 5px;");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(mainContent.getScene().getWindow());
            if (selectedFile != null) {
                imageField.setText(selectedFile.getAbsolutePath());
            }
        });

        Label prixLabel = new Label("Prix");
        prixLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField prixField = new TextField();
        prixField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField descriptionField = new TextField();
        descriptionField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Button addButton = new Button("Ajouter");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        addButton.setOnAction(e -> handleAddMarchandise(nomField, imageField, prixField, descriptionField));

        form.add(nomLabel, 0, 0);
        form.add(nomField, 1, 0);
        form.add(imageLabel, 0, 1);
        form.add(imageField, 1, 1);
        form.add(browseButton, 2, 1);
        form.add(prixLabel, 0, 2);
        form.add(prixField, 1, 2);
        form.add(descriptionLabel, 0, 3);
        form.add(descriptionField, 1, 3);
        form.add(addButton, 1, 4);

        mainContent.getChildren().add(form);
    }

    @FXML
    private void showMarchandiseList() {
        mainContent.getChildren().clear();

        TableView<Marchandise> marchandiseTable = new TableView<>();
        marchandiseTable.setPrefHeight(400.0);
        marchandiseTable.setPrefWidth(700.0);
        marchandiseTable.setStyle("-fx-background-color: white; -fx-border-radius: 10px;");

        TableColumn<Marchandise, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setPrefWidth(150);
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Marchandise, Double> prixColumn = new TableColumn<>("Prix");
        prixColumn.setPrefWidth(100);
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        TableColumn<Marchandise, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setPrefWidth(300);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Marchandise, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(150);
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                updateButton.setStyle("-fx-background-color: #36f439; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5; -fx-border-radius: 5px;");
                deleteButton.setStyle("-fx-background-color: #ea4242; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5; -fx-border-radius: 5px;");

                updateButton.setOnAction(e -> {
                    Marchandise marchandise = getTableView().getItems().get(getIndex());
                    showUpdateMarchandiseForm(marchandise);
                });

                deleteButton.setOnAction(e -> {
                    Marchandise marchandise = getTableView().getItems().get(getIndex());
                    handleDeleteMarchandise(marchandise);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, updateButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        marchandiseTable.getColumns().addAll(nomColumn, prixColumn, descriptionColumn, actionColumn);

        loadMarchandises();
        marchandiseTable.setItems(marchandiseList);

        mainContent.getChildren().add(marchandiseTable);
    }

    private void showUpdateMarchandiseForm(Marchandise marchandise) {
        mainContent.getChildren().clear();

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(javafx.geometry.Pos.CENTER);

        Label nomLabel = new Label("Nom");
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField nomField = new TextField(marchandise.getNom());
        nomField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Label imageLabel = new Label("Image");
        imageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField imageField = new TextField("Image already set (select new image if needed)");
        imageField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        imageField.setEditable(false);

        Button browseButton = new Button("Parcourir...");
        browseButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8; -fx-border-radius: 5px;");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(mainContent.getScene().getWindow());
            if (selectedFile != null) {
                imageField.setText(selectedFile.getAbsolutePath());
            }
        });

        Label prixLabel = new Label("Prix");
        prixLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField prixField = new TextField(String.valueOf(marchandise.getPrix()));
        prixField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField descriptionField = new TextField(marchandise.getDescription());
        descriptionField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #36f439; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        updateButton.setOnAction(e -> handleUpdateMarchandise(marchandise, nomField, imageField, prixField, descriptionField));

        Button cancelButton = new Button("Annuler");
        cancelButton.setStyle("-fx-background-color: #ea4242; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        cancelButton.setOnAction(e -> showMarchandiseList());

        HBox buttonBox = new HBox(10, updateButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        form.add(nomLabel, 0, 0);
        form.add(nomField, 1, 0);
        form.add(imageLabel, 0, 1);
        form.add(imageField, 1, 1);
        form.add(browseButton, 2, 1);
        form.add(prixLabel, 0, 2);
        form.add(prixField, 1, 2);
        form.add(descriptionLabel, 0, 3);
        form.add(descriptionField, 1, 3);
        form.add(buttonBox, 1, 4);

        mainContent.getChildren().add(form);
    }

    @FXML
    private void showDonList() {
        mainContent.getChildren().clear();

        TableView<Don> donTable = new TableView<>();
        donTable.setPrefHeight(400.0);
        donTable.setPrefWidth(700.0);
        donTable.setStyle("-fx-background-color: white; -fx-border-radius: 10px;");

        TableColumn<Don, String> idEvenementColumn = new TableColumn<>("Événement");
        idEvenementColumn.setPrefWidth(120);
        idEvenementColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getEventTitle(cellData.getValue().getIdEvenement())));

        TableColumn<Don, Double> montantColumn = new TableColumn<>("Montant");
        montantColumn.setPrefWidth(100);
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

        TableColumn<Don, String> typeDonColumn = new TableColumn<>("Type Don");
        typeDonColumn.setPrefWidth(100);
        typeDonColumn.setCellValueFactory(new PropertyValueFactory<>("typeDon"));

        TableColumn<Don, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setPrefWidth(150);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Don, String> methodePaiementColumn = new TableColumn<>("Méthode Paiement");
        methodePaiementColumn.setPrefWidth(120);
        methodePaiementColumn.setCellValueFactory(new PropertyValueFactory<>("methodePaiement"));

        TableColumn<Don, Date> dateDonColumn = new TableColumn<>("Date");
        dateDonColumn.setPrefWidth(120);
        dateDonColumn.setCellValueFactory(new PropertyValueFactory<>("dateDon"));

        TableColumn<Don, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(150);
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                updateButton.setStyle("-fx-background-color: #36f439; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5; -fx-border-radius: 5px;");
                deleteButton.setStyle("-fx-background-color: #ea4242; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5; -fx-border-radius: 5px;");

                updateButton.setOnAction(e -> {
                    Don don = getTableView().getItems().get(getIndex());
                    showUpdateDonForm(don);
                });

                deleteButton.setOnAction(e -> {
                    Don don = getTableView().getItems().get(getIndex());
                    handleDeleteDon(don);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, updateButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        donTable.getColumns().addAll(idEvenementColumn, montantColumn, typeDonColumn, descriptionColumn, methodePaiementColumn, dateDonColumn, actionColumn);

        loadDons();
        donTable.setItems(donList);

        mainContent.getChildren().add(donTable);
    }

    private void showUpdateDonForm(Don don) {
        mainContent.getChildren().clear();

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(javafx.geometry.Pos.CENTER);

        Label evenementLabel = new Label("Événement");
        evenementLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        ComboBox<Evenement> evenementCombo = new ComboBox<>(evenementList);
        evenementCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        evenementCombo.setPrefWidth(200);
        evenementCombo.setCellFactory(p -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getTitre());
            }
        });
        evenementCombo.setButtonCell(new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getTitre());
            }
        });

        Label montantLabel = new Label("Montant");
        montantLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField montantField = new TextField(String.valueOf(don.getMontant()));
        montantField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Label typeDonLabel = new Label("Type Don");
        typeDonLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        ComboBox<String> typeDonCombo = new ComboBox<>(FXCollections.observableArrayList("argent", "matériel", "autre"));
        typeDonCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        typeDonCombo.setPrefWidth(200);
        typeDonCombo.setValue(don.getTypeDon());

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        TextField descriptionField = new TextField(don.getDescription());
        descriptionField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");

        Label methodePaiementLabel = new Label("Méthode Paiement");
        methodePaiementLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        ComboBox<String> methodePaiementCombo = new ComboBox<>(FXCollections.observableArrayList("carte", "virement", "espèces", "crypto"));
        methodePaiementCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        methodePaiementCombo.setPrefWidth(200);
        methodePaiementCombo.setValue(don.getMethodePaiement());

        Label dateDonLabel = new Label("Date");
        dateDonLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        DatePicker dateDonPicker = new DatePicker();
        dateDonPicker.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        dateDonPicker.setPrefWidth(200);
        dateDonPicker.setValue(don.getDateDon().toLocalDate());

        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #36f439; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        updateButton.setOnAction(e -> handleUpdateDon(don, evenementCombo, montantField, typeDonCombo, descriptionField, methodePaiementCombo, dateDonPicker));

        Button cancelButton = new Button("Annuler");
        cancelButton.setStyle("-fx-background-color: #ea4242; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        cancelButton.setOnAction(e -> showDonList());

        HBox buttonBox = new HBox(10, updateButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        form.add(evenementLabel, 0, 0);
        form.add(evenementCombo, 1, 0);
        form.add(montantLabel, 0, 1);
        form.add(montantField, 1, 1);
        form.add(typeDonLabel, 0, 2);
        form.add(typeDonCombo, 1, 2);
        form.add(descriptionLabel, 0, 3);
        form.add(descriptionField, 1, 3);
        form.add(methodePaiementLabel, 0, 4);
        form.add(methodePaiementCombo, 1, 4);
        form.add(dateDonLabel, 0, 5);
        form.add(dateDonPicker, 1, 5);
        form.add(buttonBox, 1, 6);

        loadEvenements();
        evenementCombo.setItems(evenementList);

        try {
            Evenement event = serviceEvenement.getEvenementById(don.getIdEvenement());
            if (event != null) {
                evenementCombo.setValue(event);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement de l'événement: " + e.getMessage());
        }

        mainContent.getChildren().add(form);
    }

    //controle de saisie
    private void handleAddMarchandise(TextField nomField, TextField imageField, TextField prixField, TextField descriptionField) {
        try {
            String nom = nomField.getText() != null ? nomField.getText().trim() : "";
            String imagePath = imageField.getText() != null ? imageField.getText().trim() : "";
            String description = descriptionField.getText() != null ? descriptionField.getText().trim() : "";
            String prixText = prixField.getText() != null ? prixField.getText().trim() : "";

            if (nom.isEmpty() || imagePath.isEmpty() || description.isEmpty() || prixText.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis");
                return;
            }

            double prix = Double.parseDouble(prixText);
            if (prix <= 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif supérieur à 0");
                return;
            }

            byte[] imageBytes = null;
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    imageBytes = fis.readAllBytes();
                }
            } else {
                showAlert("Erreur", "Le fichier image sélectionné n'existe pas");
                return;
            }

            Marchandise marchandise = new Marchandise(nom, imageBytes, prix, description);
            marchandiseService.ajouter(marchandise);
            showAlert("Succès", "Marchandise ajoutée avec succès!");
            nomField.clear();
            imageField.clear();
            prixField.clear();
            descriptionField.clear();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide (nombre positif)");
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la lecture du fichier image: " + e.getMessage());
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private void handleUpdateMarchandise(Marchandise marchandise, TextField nomField, TextField imageField, TextField prixField, TextField descriptionField) {
        try {
            String nom = nomField.getText() != null ? nomField.getText().trim() : "";
            String imagePath = imageField.getText() != null ? imageField.getText().trim() : "";
            String description = descriptionField.getText() != null ? descriptionField.getText().trim() : "";
            String prixText = prixField.getText() != null ? prixField.getText().trim() : "";

            if (nom.isEmpty() || description.isEmpty() || prixText.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis");
                return;
            }

            double prix = Double.parseDouble(prixText);
            if (prix <= 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif supérieur à 0");
                return;
            }

            byte[] imageBytes = marchandise.getImage();
            if (!imagePath.isEmpty() && !imagePath.equals("Image already set (select new image if needed)")) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        imageBytes = fis.readAllBytes();
                    }
                } else {
                    showAlert("Erreur", "Le fichier image sélectionné n'existe pas");
                    return;
                }
            }

            marchandiseService.modifier(marchandise.getId(), nom, imageBytes, prix, description);
            showAlert("Succès", "Marchandise modifiée avec succès!");
            showMarchandiseList();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide (nombre positif)");
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la lecture du fichier image: " + e.getMessage());
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void handleDeleteMarchandise(Marchandise marchandise) {
        // Show confirmation dialog before deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la marchandise ?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer la marchandise \"" + marchandise.getNom() + "\" ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                marchandiseService.supprimer(marchandise.getId());
                showAlert("Succès", "Marchandise supprimée avec succès!");
                loadMarchandises();
                showMarchandiseList();
            } catch (SQLException e) {
                showAlert("Erreur", e.getMessage());
            }
        }
    }

    private void handleUpdateDon(Don don, ComboBox<Evenement> evenementCombo, TextField montantField, ComboBox<String> typeDonCombo,
                                 TextField descriptionField, ComboBox<String> methodePaiementCombo, DatePicker dateDonPicker) {
        try {
            Evenement selectedEvent = evenementCombo.getValue();
            if (selectedEvent == null) {
                showAlert("Erreur", "Veuillez sélectionner un événement");
                return;
            }
            String montantText = montantField.getText();
            if (montantText == null || montantText.trim().isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un montant");
                return;
            }
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être supérieur à 0");
                return;
            }
            String typeDon = typeDonCombo.getValue();
            if (typeDon == null) {
                showAlert("Erreur", "Veuillez sélectionner un type de don");
                return;
            }
            String description = descriptionField.getText() != null ? descriptionField.getText().trim() : "";
            if (description.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer une description");
                return;
            }
            String methodePaiement = methodePaiementCombo.getValue();
            if (methodePaiement == null) {
                showAlert("Erreur", "Veuillez sélectionner une méthode de paiement");
                return;
            }
            if (dateDonPicker.getValue() == null) {
                showAlert("Erreur", "Veuillez sélectionner une date");
                return;
            }

            serviceDon.modifier(don.getId(), selectedEvent.getId(), montant, typeDon, description, methodePaiement, Date.valueOf(dateDonPicker.getValue()));
            showAlert("Succès", "Don modifié avec succès!");
            showDonList();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "Veuillez sélectionner une date valide");
        }
    }

    private void handleDeleteDon(Don don) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le don ?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce don ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceDon.supprimer(don.getId());
                showAlert("Succès", "Don supprimé avec succès!");
                loadDons();
                showDonList();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    private String getEventTitle(int idEvenement) {
        try {
            Evenement event = serviceEvenement.getEvenementById(idEvenement);
            return event != null ? event.getTitre() : "Événement inconnu";
        } catch (SQLException e) {
            return "Erreur: " + e.getMessage();
        }
    }

    private void loadDons() {
        try {
            donList.clear();
            List<Don> dons = serviceDon.recuperer();
            donList.addAll(dons);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des dons: " + e.getMessage());
        }
    }

    private void loadMarchandises() {
        try {
            marchandiseList.clear();
            List<Marchandise> marchandises = marchandiseService.recuperer();
            marchandiseList.addAll(marchandises);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des marchandises: " + e.getMessage());
        }
    }

    private void loadEvenements() {
        try {
            evenementList.clear();
            List<Evenement> evenements = serviceEvenement.getAllEvenements(); // Ensure this method exists in ServiceEvenement
            evenementList.addAll(evenements);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des événements: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}