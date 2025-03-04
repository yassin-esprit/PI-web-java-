package services;

import models.Marchandise;
import tools.MyDB;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarchandiseService {
    private Connection cnx;

    public MarchandiseService() {
        cnx = MyDB.getInstance().getConnection();
    }

    // Add a new merchandise with image as BLOB
    public void ajouter(Marchandise m) throws SQLException, IOException {
        String sql = "INSERT INTO marchandise (nom, image, prix, description) VALUES (?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ste.setString(1, m.getNom());
        if (m.getImage() != null) {
            ste.setBytes(2, m.getImage());
        } else {
            ste.setNull(2, Types.BLOB);
        }
        ste.setDouble(3, m.getPrix());
        ste.setString(4, m.getDescription());
        ste.executeUpdate();

        ResultSet rs = ste.getGeneratedKeys();
        if (rs.next()) {
            m.setId(rs.getInt(1));
        }
        System.out.println("✅ Marchandise ajoutée avec succès ! ID: " + m.getId());
    }

    // Update an existing merchandise with image as BLOB
    public void modifier(int id, String nom, byte[] image, double prix, String description) throws SQLException {
        String sql = "UPDATE marchandise SET nom=?, image=?, prix=?, description=? WHERE id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);

        ste.setString(1, nom);
        if (image != null) {
            ste.setBytes(2, image);
        } else {
            ste.setNull(2, Types.BLOB);
        }
        ste.setDouble(3, prix);
        ste.setString(4, description);
        ste.setInt(5, id);
        ste.executeUpdate();
        System.out.println("✅ Marchandise modifiée avec succès !");
    }

    // In MarchandiseService.java
    public void supprimer(int id) throws SQLException {
        // Check if there are any associated purchases
        String checkSql = "SELECT COUNT(*) FROM achat WHERE idMarchandise = ?";
        PreparedStatement checkStmt = cnx.prepareStatement(checkSql);
        checkStmt.setInt(1, id);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            throw new SQLException("Impossible de supprimer la marchandise : elle est associée à des achats existants.");
        }

        // If no associated purchases, proceed with deletion
        String sql = "DELETE FROM marchandise WHERE id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("✅ Marchandise supprimée avec succès !");
    }

    // Retrieve all merchandise with image as BLOB
    public List<Marchandise> recuperer() throws SQLException {
        String sql = "SELECT * FROM marchandise";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Marchandise> marchandises = new ArrayList<>();

        while (rs.next()) {
            Marchandise m = new Marchandise(
                    rs.getString("nom"),
                    rs.getBytes("image"),
                    rs.getDouble("prix"),
                    rs.getString("description")
            );
            m.setId(rs.getInt("id"));
            marchandises.add(m);
        }
        return marchandises;
    }

    // Record a purchase with email
    public void acheter(int idMarchandise, double montant, String methodePaiement, String email) throws SQLException {
        String sql = "INSERT INTO achat (idMarchandise, montant, methodePaiement, dateAchat, email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idMarchandise);
        ste.setDouble(2, montant);
        ste.setString(3, methodePaiement);
        ste.setDate(4, new java.sql.Date(System.currentTimeMillis()));
        ste.setString(5, email);
        ste.executeUpdate();
        System.out.println("✅ Achat enregistré avec succès !");
    }
}