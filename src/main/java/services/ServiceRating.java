package services;

import models.Rating;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRating {

    private Connection cnx;

    public ServiceRating() {
        cnx = MyDB.getInstance().getConnection();
    }

    // Add a rating and review
    public void addRating(Rating rating) throws SQLException {
        String sql = "INSERT INTO ratings (event_id, user_id, rating, review) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, rating.getEventId());
            pst.setInt(2, rating.getUserId());
            pst.setInt(3, rating.getRating());
            pst.setString(4, rating.getReview());
            pst.executeUpdate();
        }
    }

    // Get all ratings for an event
    public List<Rating> getRatingsForEvent(int eventId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE event_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, eventId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Rating rating = new Rating(
                        rs.getInt("id"),
                        rs.getInt("event_id"),
                        rs.getInt("user_id"),
                        rs.getInt("rating"),
                        rs.getString("review"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                ratings.add(rating);
            }
        }
        return ratings;
    }

    // Get average rating for an event
    public double getAverageRating(int eventId) throws SQLException {
        String sql = "SELECT AVG(rating) AS average FROM ratings WHERE event_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, eventId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average");
            }
        }
        return 0.0;
    }
}