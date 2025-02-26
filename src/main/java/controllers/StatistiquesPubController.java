package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.publicites;
import models.statut_pub;
import services.ServicePublicite;

import java.sql.SQLException;
import java.util.List;

public class StatistiquesPubController {

    @FXML
    private Label labelStatistiques;

    private ServicePublicite servicePublicites = new ServicePublicite();

    public void initialize() {
        try {
            // Récupérer les publicités actives et inactives
            List<publicites> actives = this.servicePublicites.recupererParStatut(statut_pub.active);
            List<publicites> inactives = this.servicePublicites.recupererParStatut(statut_pub.inactif);

            // Afficher les statistiques
            labelStatistiques.setText("Publicités actives : " + actives.size() + "\nPublicités inactives : " + inactives.size());
        } catch (SQLException e) {
            e.printStackTrace();
            labelStatistiques.setText("Erreur de récupération des statistiques.");
        }
    }
}
