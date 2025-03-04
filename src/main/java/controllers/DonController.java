package controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Don;
import models.Evenement;
import models.Marchandise;
import services.MarchandiseService;
import services.ServiceDon;
import services.ServiceEvenement;
import tools.GMailer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DonController implements Initializable {
    @FXML private VBox mainContent;

    private MarchandiseService marchandiseService = new MarchandiseService();
    private ServiceDon serviceDon = new ServiceDon();
    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    private ObservableList<Marchandise> marchandiseList = FXCollections.observableArrayList();
    private ObservableList<Marchandise> filteredMarchandiseList = FXCollections.observableArrayList();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();
    private Marchandise selectedMarchandise;

    private ComboBox<Evenement> evenementCombo;
    private TextField montantField;
    private ComboBox<String> typeDonCombo;
    private TextField descriptionField;
    private ComboBox<String> methodePaiementDonCombo;
    private DatePicker dateDonPicker;

    private TextField searchField;
    private ComboBox<String> sortCriteriaCombo;
    private ComboBox<String> sortOrderCombo;
    private FlowPane marchandiseFlowPane;
    private ComboBox<Marchandise> marchandiseCombo;
    private ComboBox<String> methodePaiementCombo;
    private TextField emailField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showMarchandisesView();
    }

    @FXML
    private void showMarchandisesView() {
        mainContent.getChildren().clear();

        VBox merchandiseView = new VBox(20);

        Label header = new Label("Acheter des Marchandises");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        HBox searchFilterBox = new HBox(15);
        searchFilterBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label searchLabel = new Label("Rechercher:");
        searchLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        searchField = new TextField();
        searchField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");
        searchField.setPrefWidth(250); // Increased width for better usability
        searchField.setPromptText("Entrez le nom du produit...");

        Label sortLabel = new Label("Trier par:");
        sortLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        sortCriteriaCombo = new ComboBox<>(FXCollections.observableArrayList("Prix", "Nom"));
        sortCriteriaCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");
        sortCriteriaCombo.setValue("Prix");

        Label orderLabel = new Label("Ordre:");
        orderLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        sortOrderCombo = new ComboBox<>(FXCollections.observableArrayList("Ascendant", "Descendant"));
        sortOrderCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");
        sortOrderCombo.setValue("Ascendant");

        searchFilterBox.getChildren().addAll(searchLabel, searchField, sortLabel, sortCriteriaCombo, orderLabel, sortOrderCombo);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400.0); // Increased height for more vertical space
        scrollPane.setPrefWidth(800.0); // Match the mainContent width

        marchandiseFlowPane = new FlowPane();
        marchandiseFlowPane.setHgap(30); // Increased spacing for better visibility
        marchandiseFlowPane.setVgap(30);
        marchandiseFlowPane.setPrefWidth(800.0); // Match the scrollPane width
        marchandiseFlowPane.setStyle("-fx-padding: 15;"); // Increased padding

        scrollPane.setContent(marchandiseFlowPane);

        GridPane purchasePane = new GridPane();
        purchasePane.setHgap(15);
        purchasePane.setVgap(15);
        purchasePane.setAlignment(javafx.geometry.Pos.CENTER);

        Label marchandiseLabel = new Label("Marchandise");
        marchandiseLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        marchandiseCombo = new ComboBox<>();
        marchandiseCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        marchandiseCombo.setPrefWidth(250); // Increased width

        Label methodePaiementLabel = new Label("Méthode Paiement");
        methodePaiementLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        methodePaiementCombo = new ComboBox<>();
        methodePaiementCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        methodePaiementCombo.setPrefWidth(250);

        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        emailField = new TextField();
        emailField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        emailField.setPrefWidth(250);

        purchasePane.add(marchandiseLabel, 0, 0);
        purchasePane.add(marchandiseCombo, 1, 0);
        purchasePane.add(methodePaiementLabel, 0, 1);
        purchasePane.add(methodePaiementCombo, 1, 1);
        purchasePane.add(emailLabel, 0, 2);
        purchasePane.add(emailField, 1, 2);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        Button purchaseButton = new Button("Acheter");
        purchaseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        purchaseButton.setOnAction(e -> handlePurchase());
        buttonBox.getChildren().add(purchaseButton);

        merchandiseView.getChildren().addAll(header, searchFilterBox, scrollPane, purchasePane, buttonBox);
        mainContent.getChildren().add(merchandiseView);

        loadMarchandises();
        filteredMarchandiseList.clear();
        filteredMarchandiseList.addAll(marchandiseList);
        updateMarchandiseCards();

        methodePaiementCombo.setItems(FXCollections.observableArrayList("carte", "virement", "espèces", "crypto"));
        marchandiseCombo.setItems(filteredMarchandiseList);
        marchandiseCombo.setCellFactory(p -> new ListCell<Marchandise>() {
            @Override
            protected void updateItem(Marchandise item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNom());
            }
        });
        marchandiseCombo.setButtonCell(new ListCell<Marchandise>() {
            @Override
            protected void updateItem(Marchandise item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNom());
            }
        });

        searchField.textProperty().addListener((obs, oldValue, newValue) -> updateMarchandiseCards());
        sortCriteriaCombo.valueProperty().addListener((obs, oldValue, newValue) -> updateMarchandiseCards());
        sortOrderCombo.valueProperty().addListener((obs, oldValue, newValue) -> updateMarchandiseCards());
    }

    @FXML
    private void showAddDonForm() {
        mainContent.getChildren().clear();

        VBox donView = new VBox(20);

        Label header = new Label("Ajouter un Don");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(javafx.geometry.Pos.CENTER);

        Label evenementLabel = new Label("Événement");
        evenementLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        evenementCombo = new ComboBox<>();
        evenementCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        evenementCombo.setPrefWidth(200);

        Label montantLabel = new Label("Montant");
        montantLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        montantField = new TextField();
        montantField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        montantField.setPrefWidth(200);

        Label typeDonLabel = new Label("Type Don");
        typeDonLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        typeDonCombo = new ComboBox<>();
        typeDonCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        typeDonCombo.setPrefWidth(200);

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        descriptionField = new TextField();
        descriptionField.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        descriptionField.setPrefWidth(200);

        Label methodePaiementLabel = new Label("Méthode Paiement");
        methodePaiementLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        methodePaiementDonCombo = new ComboBox<>();
        methodePaiementDonCombo.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        methodePaiementDonCombo.setPrefWidth(200);

        Label dateDonLabel = new Label("Date");
        dateDonLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        dateDonPicker = new DatePicker();
        dateDonPicker.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        dateDonPicker.setPrefWidth(200);

        form.add(evenementLabel, 0, 0);
        form.add(evenementCombo, 1, 0);
        form.add(montantLabel, 0, 1);
        form.add(montantField, 1, 1);
        form.add(typeDonLabel, 0, 2);
        form.add(typeDonCombo, 1, 2);
        form.add(descriptionLabel, 0, 3);
        form.add(descriptionField, 1, 3);
        form.add(methodePaiementLabel, 0, 4);
        form.add(methodePaiementDonCombo, 1, 4);
        form.add(dateDonLabel, 0, 5);
        form.add(dateDonPicker, 1, 5);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        Button addButton = new Button("Ajouter");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 30px; -fx-border-radius: 8px;");
        addButton.setOnAction(e -> handleAddDon());
        buttonBox.getChildren().add(addButton);

        donView.getChildren().addAll(header, form, buttonBox);
        mainContent.getChildren().add(donView);

        typeDonCombo.setItems(FXCollections.observableArrayList("argent", "matériel", "autre"));
        methodePaiementDonCombo.setItems(FXCollections.observableArrayList("carte", "virement", "espèces", "crypto"));
        loadEvenements();
        evenementCombo.setItems(evenementList);
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
            List<Evenement> evenements = serviceEvenement.getAllEvenements();
            evenementList.addAll(evenements);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des événements: " + e.getMessage());
        }
    }

    private void updateMarchandiseCards() {
        marchandiseFlowPane.getChildren().clear();

        String searchText = searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
        List<Marchandise> filteredList = marchandiseList.stream()
                .filter(m -> m.getNom().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        String sortCriteria = sortCriteriaCombo.getValue();
        String sortOrder = sortOrderCombo.getValue();
        Comparator<Marchandise> comparator = null;

        if ("Prix".equals(sortCriteria)) {
            comparator = Comparator.comparingDouble(Marchandise::getPrix);
        } else if ("Nom".equals(sortCriteria)) {
            comparator = Comparator.comparing(Marchandise::getNom, String.CASE_INSENSITIVE_ORDER);
        }

        if (comparator != null) {
            if ("Descendant".equals(sortOrder)) {
                comparator = comparator.reversed();
            }
            filteredList.sort(comparator);
        }

        filteredMarchandiseList.clear();
        filteredMarchandiseList.addAll(filteredList);
        marchandiseCombo.setItems(filteredMarchandiseList);

        for (Marchandise marchandise : filteredMarchandiseList) {
            VBox card = createMarchandiseCard(marchandise);
            marchandiseFlowPane.getChildren().add(card);
        }
    }

    private VBox createMarchandiseCard(Marchandise marchandise) {
        VBox card = new VBox(15); // Increased spacing between elements
        card.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-background-radius: 10px; " +
                "-fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2); " +
                "-fx-cursor: hand;");
        card.setPrefWidth(250); // Increased card width
        card.setPrefHeight(300); // Increased card height

        ImageView imageView = new ImageView();
        if (marchandise.getImage() != null) {
            Image image = new Image(new ByteArrayInputStream(marchandise.getImage()), 200, 120, true, true); // Increased image size
            imageView.setImage(image);
        } else {
            imageView.setImage(new Image("https://via.placeholder.com/200x120.png?text=No+Image", 200, 120, true, true));
        }
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);

        Label nomLabel = new Label(marchandise.getNom());
        nomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;"); // Increased font size

        Label prixLabel = new Label("Prix: " + marchandise.getPrix() + " €");
        prixLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;"); // Increased font size

        Label descriptionLabel = new Label(marchandise.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #999;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxHeight(60); // Increased height for description

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; -fx-padding: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 3);");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; -fx-padding: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        });

        card.setOnMouseClicked(e -> {
            selectedMarchandise = marchandise;
            marchandiseCombo.setValue(marchandise);
            marchandiseFlowPane.getChildren().forEach(node -> node.setStyle("-fx-background-color: white; -fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; -fx-padding: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"));
            card.setStyle("-fx-background-color: #e0f7fa; -fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; -fx-padding: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 3);");
        });

        card.getChildren().addAll(imageView, nomLabel, prixLabel, descriptionLabel);
        return card;
    }

    @FXML
    private void handlePurchase() {
        try {
            Marchandise selectedMarchandise = marchandiseCombo.getValue();
            if (selectedMarchandise == null) {
                showAlert("Erreur", "Veuillez sélectionner une marchandise");
                return;
            }
            String methodePaiement = methodePaiementCombo.getValue();
            if (methodePaiement == null) {
                showAlert("Erreur", "Veuillez sélectionner une méthode de paiement");
                return;
            }
            String email = emailField.getText() != null ? emailField.getText().trim() : "";
            if (email.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer votre email");
                return;
            }
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!Pattern.matches(emailRegex, email)) {
                showAlert("Erreur", "Veuillez entrer un email valide (ex: exemple@domaine.com)");
                return;
            }

            marchandiseService.acheter(selectedMarchandise.getId(), selectedMarchandise.getPrix(), methodePaiement, email);

            File pdfFile = generatePurchaseReceipt(email, selectedMarchandise, methodePaiement);
            GMailer gMailer = new GMailer();
            String subject = "Confirmation de votre commande";
            String messageText = "Bonjour,\n\nVotre commande a été effectuée avec succès !\n\n" +
                    "Merci d'avoir effectué un achat chez nous. Vous trouverez ci-joint un reçu de votre commande.\n\n" +
                    "Cordialement,\nL'équipe de la boutique";
            gMailer.sendMail(email, subject, messageText, pdfFile);

            if (pdfFile.exists()) {
                pdfFile.delete();
            }

            showAlert("Succès", "Achat effectué avec succès! Un email de confirmation a été envoyé à " + email);
            clearFields();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'achat: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddDon() {
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
            String methodePaiement = methodePaiementDonCombo.getValue();
            if (methodePaiement == null) {
                showAlert("Erreur", "Veuillez sélectionner une méthode de paiement");
                return;
            }
            if (dateDonPicker.getValue() == null) {
                showAlert("Erreur", "Veuillez sélectionner une date");
                return;
            }

            Don don = new Don(
                    selectedEvent.getId(),
                    montant,
                    typeDon,
                    description,
                    methodePaiement,
                    Date.valueOf(dateDonPicker.getValue())
            );
            serviceDon.ajouter(don);
            showAlert("Succès", "Don ajouté avec succès!");
            clearDonFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "Veuillez sélectionner une date valide");
        }
    }

    private File generatePurchaseReceipt(String recipientEmail, Marchandise marchandise, String methodePaiement) throws Exception {
        String fileName = "purchase_receipt_" + System.currentTimeMillis() + ".pdf";
        File pdfFile = new File(fileName);

        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Reçu de Commande").setFontSize(18).setBold());
        document.add(new Paragraph("-------------------------"));
        document.add(new Paragraph("Email du destinataire: " + recipientEmail));
        document.add(new Paragraph("Date: " + new java.util.Date().toString()));
        document.add(new Paragraph("\nDétails de la marchandise:"));
        document.add(new Paragraph("Nom: " + marchandise.getNom()));
        document.add(new Paragraph("Prix: " + marchandise.getPrix() + " €"));
        document.add(new Paragraph("Description: " + marchandise.getDescription()));
        document.add(new Paragraph("Méthode de paiement: " + methodePaiement));
        document.add(new Paragraph("\nMerci pour votre achat !"));

        document.close();
        return pdfFile;
    }

    private void clearFields() {
        marchandiseCombo.setValue(null);
        methodePaiementCombo.setValue(null);
        emailField.clear();
        selectedMarchandise = null;
        marchandiseFlowPane.getChildren().forEach(node -> node.setStyle("-fx-background-color: white; -fx-border-radius: 10px; " +
                "-fx-background-radius: 10px; -fx-padding: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"));
    }

    private void clearDonFields() {
        evenementCombo.setValue(null);
        montantField.clear();
        typeDonCombo.setValue(null);
        descriptionField.clear();
        methodePaiementDonCombo.setValue(null);
        dateDonPicker.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}