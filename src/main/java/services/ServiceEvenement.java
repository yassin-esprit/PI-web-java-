package services;

import models.evenement;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement
        implements IServices<evenement> {
    private Connection cnx;

    public ServiceEvenement() {
        cnx = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(evenement e) throws SQLException {
        String sql = "insert into evenement (titre, type, description, date, lieu, nbrplaces, sponsor) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, e.getTitre());
        ste.setString(2, e.getType().name()); // Convert enum to String
        ste.setString(3, e.getDescription());
        ste.setDate(4, new java.sql.Date(e.getDate().getTime()));
        ste.setString(5, e.getLieu());
        ste.setInt(6, e.getNbrplaces());
        ste.setInt(7, e.getSponsor());
        ste.executeUpdate();
        System.out.println("Evenement ajouté");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from evenement where id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Evenement supprimé");
    }

    @Override
    public void modifier(int id, String titre, String type, String description, Date date, String lieu, int nbrplaces,
            int sponsor) throws SQLException {
        String sql = "update evenement set titre=?, type=?, description=?, date=?, lieu=?, nbrplaces=?, sponsor=? where id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, titre);
        ste.setString(2, type);
        ste.setString(3, description);
        ste.setDate(4, new java.sql.Date(date.getTime()));
        ste.setString(5, lieu);
        ste.setInt(6, nbrplaces);
        ste.setInt(7, sponsor);
        ste.setInt(8, id);
        ste.executeUpdate();
        System.out.println("Evenement modifié");
    }

    @Override
    public List<evenement> recuperer() throws SQLException {
        String sql = "select * from evenement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<evenement> evenements = new ArrayList<>();
        while (rs.next()) {
            evenement e = new evenement(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    models.type_evenement.valueOf(rs.getString("type")),
                    rs.getString("description"),
                    rs.getDate("date"),
                    rs.getString("lieu"),
                    rs.getInt("nbrplaces"),
                    rs.getInt("sponsor"));
            evenements.add(e);
        }
        return evenements;
    }
}