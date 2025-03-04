package services;

import models.Don;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDon {
    private Connection cnx;

    public ServiceDon() {
        cnx = MyDB.getInstance().getConnection();
    }

    public void ajouter(Don d) throws SQLException {
        String sql = "INSERT INTO don (idEvenement, montant, typeDon, description, methodePaiement, dateDon) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ste.setInt(1, d.getIdEvenement());
        ste.setDouble(2, d.getMontant());
        ste.setString(3, d.getTypeDon());
        ste.setString(4, d.getDescription());
        ste.setString(5, d.getMethodePaiement());
        ste.setDate(6, new java.sql.Date(d.getDateDon().getTime()));
        ste.executeUpdate();

        ResultSet rs = ste.getGeneratedKeys();
        if (rs.next()) {
            d.setId(rs.getInt(1));
        }
        System.out.println("✅ Don ajouté avec succès ! ID: " + d.getId());
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM don WHERE id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("✅ Don supprimé avec succès !");
    }

    public void modifier(int id, int idEvenement, double montant, String typeDon, String description,
                         String methodePaiement, Date dateDon) throws SQLException {
        String sql = "UPDATE don SET idEvenement=?, montant=?, typeDon=?, description=?, methodePaiement=?, dateDon=? WHERE id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEvenement);
        ste.setDouble(2, montant);
        ste.setString(3, typeDon);
        ste.setString(4, description);
        ste.setString(5, methodePaiement);
        ste.setDate(6, new java.sql.Date(dateDon.getTime()));
        ste.setInt(7, id);
        ste.executeUpdate();
        System.out.println("✅ Don modifié avec succès !");
    }

    public List<Don> recuperer() throws SQLException {
        String sql = "SELECT * FROM don";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Don> dons = new ArrayList<>();

        while (rs.next()) {
            Don d = new Don(
                    rs.getInt("idEvenement"),
                    rs.getDouble("montant"),
                    rs.getString("typeDon"),
                    rs.getString("description"),
                    rs.getString("methodePaiement"),
                    rs.getDate("dateDon")
            );
            d.setId(rs.getInt("id"));
            dons.add(d);
        }
        return dons;
    }

    public List<Don> recupererParEvenement(int idEvenement) throws SQLException {
        String sql = "SELECT * FROM don WHERE idEvenement=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEvenement);
        ResultSet rs = ste.executeQuery();
        List<Don> dons = new ArrayList<>();

        while (rs.next()) {
            Don d = new Don(
                    rs.getInt("idEvenement"),
                    rs.getDouble("montant"),
                    rs.getString("typeDon"),
                    rs.getString("description"),
                    rs.getString("methodePaiement"),
                    rs.getDate("dateDon")
            );
            d.setId(rs.getInt("id"));
            dons.add(d);
        }
        return dons;
    }
}