package services;

import models.conferencier;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service_Conferencier implements IServices_conferencier<conferencier> {
    private Connection cnx;

    public Service_Conferencier() {
        cnx = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(conferencier c) throws SQLException {
        String sql = "insert into conferencier (id_evenement, nom, biographie, contact_email) values (?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, c.getIdEvenement());
        ste.setString(2, c.getNom());
        ste.setString(3, c.getBiographie());
        ste.setString(4, c.getContactEmail());
        ste.executeUpdate();
        System.out.println("Conferencier ajouté");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from conferencier where id_conferencier=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Conferencier supprimé");
    }

    @Override
    public void modifier(int id, int id_evenement, String nom, String biographie, String contactEmail)
            throws SQLException {
        String sql = "update conferencier id_evenement=?, set nom=?, biographie=?, contact_email=? where id_conferencier=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id_evenement);
        ste.setString(2, nom);
        ste.setString(3, biographie);
        ste.setString(4, contactEmail);
        ste.setInt(5, id);
        ste.executeUpdate();
        System.out.println("Conferencier modifié");
    }

    @Override
    public List<conferencier> recuperer() throws SQLException {
        String sql = "select * from conferencier";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<conferencier> conferenciers = new ArrayList<>();
        while (rs.next()) {
            conferencier c = new conferencier(
                    rs.getInt("id_conferencier"),
                    rs.getInt("id_evenement"),
                    rs.getString("nom"),
                    rs.getString("biographie"),
                    rs.getString("contact_email"));
            conferenciers.add(c);
        }
        return conferenciers;
    }
}