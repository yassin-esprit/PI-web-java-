package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.publicites;
import models.statut_pub;
import models.type_pub;
import services.ServicePublicite;

import java.sql.SQLException;


public class AjouterPubController {


    private final ServicePublicite sp = new ServicePublicite();

    @FXML
    private TextField idContenu;

    @FXML
    private TextField idEmplacement;

    @FXML
    private ChoiceBox<statut_pub> idStatut;

    @FXML
    private TextField idTitre;

    @FXML
    private ChoiceBox<type_pub> idType;
    public void initialize() {
        idStatut.getItems().addAll(statut_pub.values()); // Ajoute toutes les valeurs de l'énumération
        idType.getItems().addAll(type_pub.values()); // Idem pour l'énumération type_pub
    }


//    @FXML
//    public void initialize() {
//        idType.getItems().addAll("image", "video", "text");
//        idType.setValue("image");
//
//        idStatut.getItems().addAll("active","inactif");
//        idStatut.setValue("inactif");
//    }

//    @FXML
//    void ajouter(ActionEvent event) {
//        try{
//
//        sp.ajouter(new publicites());
//    }catch (SQLException e)
//        {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erreur");
//
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
//
//        }}

    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        // Récupération des valeurs des champs
        String titre = idTitre.getText();
        String contenu = idContenu.getText();
        String emplacement = idEmplacement.getText();
        String typeStr = String.valueOf((type_pub) idType.getValue());
        String statutStr = String.valueOf((statut_pub) idStatut.getValue());

        // Vérification des champs (optionnelle)
        if (titre.isEmpty() || contenu.isEmpty() || emplacement.isEmpty() || typeStr == null || statutStr == null) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        // Conversion des strings en énumérations
//        type_pub type = type_pub.valueOf(typeStr.toUpperCase());
//        statut_pub statut = statut_pub.valueOf(statutStr.toUpperCase());
           type_pub type = type_pub.valueOf(typeStr);
          statut_pub statut = statut_pub.valueOf(statutStr);


        // Création de la publicité
        publicites pub = new publicites(titre, type, contenu, emplacement, statut);

        // Ajout de la publicité via le service
        sp.ajouter(pub);

        System.out.println("Publicite ajoutee avec succes !");
    }


}
