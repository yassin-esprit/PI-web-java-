package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import models.evenement;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class AjouterEvenementController {

    @FXML
    private TextField txtTitre;

    @FXML
    private TextField txtType;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtLieu;

    @FXML
    private TextField txtNbrPlaces;

    @FXML
    private TextField txtSponsor;

    @FXML
    void addEvenement(ActionEvent event) {
        String titre = txtTitre.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        Date date = java.sql.Date.valueOf(txtDate.getText());
        String lieu = txtLieu.getText();
        int nbrPlaces = Integer.parseInt(txtNbrPlaces.getText());
        int sponsor = Integer.parseInt(txtSponsor.getText());

        ServiceEvenement se = new ServiceEvenement();
        evenement e = new evenement(titre, models.type_evenement.valueOf(type), description, date, lieu, nbrPlaces,
                sponsor);
        try {
            se.ajouter(e);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent root = loader.load();
            AfficherEvenementController ac = loader.getController();
            ac.setRTitre(titre);
            ac.setRType(type);
            ac.setRList(se.recuperer().toString());
            txtTitre.getScene().setRoot(root);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
