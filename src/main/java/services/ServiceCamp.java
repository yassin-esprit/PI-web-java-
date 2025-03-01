package services;

import models.*;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCamp implements IServiceCamp<campagnes_publicitaires> {
    private Connection cnx;

    public ServiceCamp() {
        cnx = MyDB.getInstance().getConnection();
    }

    // Méthode pour vérifier si la publicité existe
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
        // Vérification si la publicité existe
        if (!publiciteExiste(c.getPublicite().getId_pub())) {
            System.out.println("Erreur : La publicité n'existe pas.");
            return;
        }

        String sql = "INSERT INTO campagnes_publicitaires (id_camp, nom_camp, budget_camp, date_debut_camp, date_fin_camp, statut_camp, publicite_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, c.getId_camp());
        ste.setString(2, c.getNom_camp());
        ste.setInt(3, c.getBudget_camp());
        ste.setString(4, c.getDate_debut_camp());
        ste.setString(5, c.getDate_fin_camp());
        ste.setString(6, c.getStatut_camp().name()); // Enum converti en String
        ste.setInt(7, c.getPublicite().getId_pub()); // Récupérer l'ID de la publicité
        ste.executeUpdate();
        System.out.println("Campagne ajoutée");
    }

    @Override
    public void supprimer(int id_camp) throws SQLException {
        String sql = "DELETE FROM campagnes_publicitaires WHERE id_camp = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id_camp);
            ste.executeUpdate();
            System.out.println("La campagne publicitaire est supprimée avec succès !");
        }
    }

    @Override
    public void modifier(int id_camp, String nom_camp, int budget_camp, String date_debut_camp, String date_fin_camp, statut_camp statut_camp, publicites publicite) throws SQLException {
        // Vérification si la publicité existe
        if (!publiciteExiste(publicite.getId_pub())) {
            System.out.println("Erreur : La publicité n'existe pas.");
            return;
        }

        String sql = "UPDATE campagnes_publicitaires SET nom_camp = ?, budget_camp = ?, date_debut_camp = ?, date_fin_camp = ?, statut_camp = ?, publicite_id = ? WHERE id_camp = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, nom_camp);
        ste.setInt(2, budget_camp);
        ste.setString(3, date_debut_camp);
        ste.setString(4, date_fin_camp);
        ste.setString(5, statut_camp.name()); // Conversion enum en String
        ste.setInt(6, publicite.getId_pub()); // Récupérer l'ID de la publicité
        ste.setInt(7, id_camp);

        ste.executeUpdate();
        System.out.println("Campagne publicitaire modifiée");
    }
    public List<publicites> recupererPublicites() throws SQLException {
        String sql = "SELECT * FROM publicites";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<publicites> publicitesList = new ArrayList<>();
        while (rs.next()) {
            publicites pub = new publicites(
                    rs.getInt("id_pub"),
                    rs.getString("titre_pub"),
                    type_pub.valueOf(rs.getString("type_pub")),
                    rs.getString("contenu_pub"),
                    rs.getString("emplacement_pub"),
                    statut_pub.valueOf(rs.getString("statut_pub"))
            );
            publicitesList.add(pub);
        }
        return publicitesList;
    }

    @Override
    public List<campagnes_publicitaires> recuperer() throws SQLException {
        String sql = "SELECT c.*, p.id_pub, p.titre_pub, p.type_pub, p.contenu_pub, p.emplacement_pub, p.statut_pub " +
                "FROM campagnes_publicitaires c " +
                "JOIN publicites p ON c.publicite_id = p.id_pub";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<campagnes_publicitaires> campagnesPublicitaires = new ArrayList<>();
        while (rs.next()) {
            // Créer l'objet publicites
            publicites pub = new publicites(
                    rs.getInt("id_pub"),
                    rs.getString("titre_pub"),
                    type_pub.valueOf(rs.getString("type_pub")),
                    rs.getString("contenu_pub"),
                    rs.getString("emplacement_pub"),
                    statut_pub.valueOf(rs.getString("statut_pub"))
            );

            // Créer l'objet campagnes_publicitaires
            campagnes_publicitaires campagne = new campagnes_publicitaires(
                    rs.getInt("id_camp"),
                    rs.getString("nom_camp"),
                    rs.getInt("budget_camp"),
                    rs.getString("date_debut_camp"),
                    rs.getString("date_fin_camp"),
                    statut_camp.valueOf(rs.getString("statut_camp")),
                    pub // Associer l'objet publicites
            );

            campagnesPublicitaires.add(campagne);
        }
        return campagnesPublicitaires;
    }
}