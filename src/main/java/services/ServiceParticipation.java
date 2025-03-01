package services;

import models.Participation;
import tools.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipation {
    private Connection cnx;

    public ServiceParticipation() {
        cnx = MyDB.getInstance().getConnection();
    }

    public void ajouterParticipation(Participation participation) throws SQLException {
        String sql = "INSERT INTO participation (evenement_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, participation.getEvenementId());
            ste.setInt(2, participation.getUserId());
            ste.executeUpdate();
            System.out.println("Participation ajoutée avec succès !");
        }
    }

    public List<Integer> getEventsByUserId(int userId) throws SQLException {
        List<Integer> eventIds = new ArrayList<>();
        String sql = "SELECT evenement_id FROM participation WHERE user_id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, userId);
            ResultSet rs = ste.executeQuery();
            while (rs.next()) {
                eventIds.add(rs.getInt("evenement_id"));
            }
        }
        return eventIds;
    }


}