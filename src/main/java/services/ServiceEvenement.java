package services;

import models.Evenement;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {
    private Connection cnx;

    public ServiceEvenement() {
        cnx = MyDB.getInstance().getConnection();
    }

    public boolean exists(int idEvenement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, idEvenement);
            ResultSet rs = ste.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public Evenement getEvenementById(int idEvenement) throws SQLException {
        String sql = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, idEvenement);
            ResultSet rs = ste.executeQuery();
            if (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setId(rs.getInt("id"));
                evenement.setTitre(rs.getString("titre"));
                evenement.setType(rs.getString("type"));
                evenement.setDate(rs.getString("date"));
                evenement.setLieu(rs.getString("lieu"));
                evenement.setNbrPlaces(rs.getInt("nbrplaces"));
                evenement.setSponsor(rs.getInt("sponsor"));
                return evenement;
            }
        }
        return null;
    }

    public List<Evenement> getAllEvenements() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenement";
        System.out.println("Executing query: " + sql);
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setId(rs.getInt("id"));
                evenement.setTitre(rs.getString("titre"));
                evenement.setType(rs.getString("type"));
                evenement.setDate(rs.getString("date"));
                evenement.setLieu(rs.getString("lieu"));
                evenement.setNbrPlaces(rs.getInt("nbrplaces"));
                evenement.setSponsor(rs.getInt("sponsor"));
                evenements.add(evenement);
            }
            System.out.println("Found " + evenements.size() + " events.");
        } catch (SQLException e) {
            System.out.println("Error fetching events: " + e.getMessage());
            throw e;
        }
        return evenements;
    }
}