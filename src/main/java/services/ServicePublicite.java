package services;

import models.publicites;
import tools.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.statut_pub;
import models.type_pub;

public class ServicePublicite implements IServices<publicites> {
    private Connection cnx;

    public ServicePublicite() {
        cnx = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(publicites p) throws SQLException {
        // Vérification pour éviter un NullPointerException
        if (p.getType_pub() == null) {
            throw new IllegalArgumentException("Type de publicité ne peut pas être null");
        }
        if (p.getStatut_pub() == null) {
            throw new IllegalArgumentException("Statut de publicité ne peut pas être null");
        }

        String sql = "INSERT INTO publicites (titre_pub, type_pub, contenu_pub, emplacement_pub, statut_pub) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, p.getTitre_pub());
            ste.setString(2, p.getType_pub().name());  // Convert enum to String
            ste.setString(3, p.getContenu_pub());
            ste.setString(4, p.getEmplacement_pub());
            ste.setString(5, p.getStatut_pub().name());  // Convert enum to String
            ste.executeUpdate();
            System.out.println("Publicité ajoutée avec succès !");
        }
    }

    @Override
    public void supprimer(int id_pub) throws SQLException {
        String sql = "DELETE FROM publicites WHERE id_pub=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id_pub);
            ste.executeUpdate();
            System.out.println("Publicité supprimée avec succès !");
        }
    }




    @Override
    public void modifier(int id_pub, String titre_pub, type_pub type_pub, String contenu_pub, String emplacement_pub, statut_pub statut_pub) throws SQLException {
        if (type_pub == null) {
            throw new IllegalArgumentException("Type de publicité ne peut pas être null");
        }
        if (statut_pub == null) {
            throw new IllegalArgumentException("Statut de publicité ne peut pas être null");
        }

        String sql = "UPDATE publicites SET titre_pub=?, type_pub=?, contenu_pub=?, emplacement_pub=?, statut_pub=? WHERE id_pub=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, titre_pub);
            ste.setString(2, type_pub.name());
            ste.setString(3, contenu_pub);
            ste.setString(4, emplacement_pub);
            ste.setString(5, statut_pub.name());
            ste.setInt(6, id_pub);  // Correction : Ajout de l’ID manquant
            ste.executeUpdate();
            System.out.println("Publicité modifiée avec succès !");
        }
    }
    public List<publicites> recuperer() throws SQLException {
        String sql = "SELECT * FROM publicites";
        Statement ste = null;
        ResultSet rs = null;
        List<publicites> publicitesList = new ArrayList<>();

        try {
            ste = cnx.createStatement();
            rs = ste.executeQuery(sql);
            while (rs.next()) {
                publicites p = new publicites();
                p.setId_pub(rs.getInt("id_pub"));
                p.setTitre_pub(rs.getString("titre_pub"));
                p.setType_pub(type_pub.valueOf(rs.getString("type_pub")));  // Vérifiez les valeurs correspondantes dans la DB
                p.setContenu_pub(rs.getString("contenu_pub"));
                p.setEmplacement_pub(rs.getString("emplacement_pub"));
                p.setStatut_pub(statut_pub.valueOf(rs.getString("statut_pub")));  // Vérifiez les valeurs correspondantes dans la DB
                publicitesList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Affichez l'erreur dans la console pour plus de détails
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ste != null) {
                ste.close();
            }
        }

        return publicitesList;
    }

}

