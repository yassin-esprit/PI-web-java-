package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import models.Personne;
import services.ServicePersonne;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterPersonneCotroller {

    @FXML
    private TextField txtAge;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtnom;

    @FXML
    void addPerson(ActionEvent event) {
        String nom=txtnom.getText();
        String prenom=txtPrenom.getText();
        int age = Integer.parseInt(txtAge.getText());
        ServicePersonne ps = new ServicePersonne();
        Personne p = new Personne(age,nom,prenom);
        try {
            ps.ajouter(p);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            FXMLLoader loder = new FXMLLoader(getClass()
                    .getResource("/AfficherPersonne.fxml"));
            Parent root = loder.load();
            AfficherPersonnecontroller ac =
                    loder.getController();
            ac.setRnom(nom);
            ac.setRprenom(prenom);
            ac.setRlist(ps.recuperer().toString());
            txtnom.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}