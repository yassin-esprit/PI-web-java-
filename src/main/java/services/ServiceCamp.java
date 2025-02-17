package services;
import models.campagnes_publicitaires;
import models.statut_camp;
import tools.MyDB;
import java.sql.*;

import java.util.List;
import java.util.ArrayList;


import java.sql.SQLException;

public class ServiceCamp implements IServiceCamp<campagnes_publicitaires>{
    private Connection cnx;

    public  ServiceCamp() {
        cnx = MyDB.getInstance().getConnection();
    }


    // Mét hode pour vérifier si la publicite existe
    private boolean publiciteExiste(int idPublicite) throws SQLException {
        String sql = "SELECT COUNT(*) FROM publicites WHERE id_pub = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idPublicite);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0; // Retourne true si la pub existe
    }


    @Override
    public void ajouter(campagnes_publicitaires c) throws SQLException {
        // Vérification si la pub  existe
        if (!publiciteExiste(c.getPublicite_id())) {
            System.out.println("Erreur : La pub n'existe pas.");
            return;
        }


        String sql = "INSERT INTO campagnes_publicitaires (id_camp, nom_camp, budget_camp, date_debut_camp, date_fin_camp, statut_camp, publicite_id) VALUES (?, ?, ?, ?, ?,?,?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, c.getId_camp());
        ste.setString(2, c.getNom_camp());
        ste.setInt(3, c.getBudget_camp());
        ste.setString(4, c.getDate_debut_camp());
        ste.setString(5, c.getDate_fin_camp());
        ste.setString(6, c.getStatut_camp().name()); // Enum converti en String
        ste.setInt(7, c.getPublicite_id());
        ste.executeUpdate();
        System.out.println("Campagne ajouté");
    }

    @Override
    public void supprimer(int id_camp) throws SQLException {
        String sql = "DELETE FROM campagnes_publicitaires WHERE id_camp=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id_camp);
            ste.executeUpdate();
            System.out.println("La campagne publicitaire est supprimée avec succès !");
        }
    }

    @Override
    public void modifier(int id_camp, String nom_camp, int budget_camp, String date_debut_camp, String date_fin_camp, models.statut_camp statut_camp, int publicite_id) throws SQLException {
        // Vérification si la pub  existe
        if (!publiciteExiste(publicite_id)) {
            System.out.println("Erreur : La pub n'existe pas.");
            return;
        }

        String sql = "UPDATE campagnes_publicitaires SET nom_camp=? , budget_camp=? , date_debut_camp=? , date_fin_camp=?, statut_camp=? , publicite_id=?  WHERE id_camp=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, nom_camp);
        ste.setInt(2, budget_camp);
        ste.setString(3, date_debut_camp);
        ste.setString(4, date_fin_camp);
        ste.setString(5, statut_camp.name()); // Conversion enum en String
        ste.setInt(6, publicite_id);
        ste.setInt(7, id_camp);


        ste.executeUpdate();
        System.out.println("Campagne publicitaires modifiée");
    }

    @Override
    public List<campagnes_publicitaires> recuperer() throws SQLException {
        String sql = "SELECT * FROM campagnes_publicitaires";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<campagnes_publicitaires> campagnesPublicitaires= new ArrayList<>();
        while (rs.next()) {
            campagnes_publicitaires R = new campagnes_publicitaires();
            R.setId_camp(rs.getInt("id_camp")); // Correctement l'ID de réservation
            R.setPublicite_id(rs.getInt("publicite_id"));

            R.setDate_debut_camp(rs.getString("date_debut_camp"));

            try {
                String statut = rs.getString("statut_camp");
                // Convertir la chaîne en majuscules avant d'appeler valueOf
                R.setStatut_camp(statut_camp.valueOf(statut.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : Statut invalide pour la réservation (id=" + R.getId_camp() + ")");
                // Vous pouvez décider de gérer cela de manière différente, par exemple définir un statut par défaut
            }

            campagnesPublicitaires.add(R);
        }
        return campagnesPublicitaires;
    }

}
