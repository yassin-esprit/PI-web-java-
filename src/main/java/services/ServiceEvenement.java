package services;

import models.evenement;
import models.type_evenement;
import tools.MyDB;
import utils.EmailUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement
        implements IServicese<evenement> {
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
        ste.setDate(4, Date.valueOf(e.getDate()));
        ste.setString(5, e.getLieu());
        ste.setInt(6, e.getNbrplaces());
        ste.setInt(7, e.getSponsor());
        ste.executeUpdate();
        System.out.println("Evenement ajouté");
    }


    private void notifyParticipants(int eventId, String subject, String body) throws SQLException {
        System.out.println("Fetching participants for event ID: " + eventId);

        // Fetch participants for the event
        String sql = "SELECT p.email FROM user p " +
                "JOIN participation pt ON p.userId = pt.user_id " +
                "WHERE pt.evenement_id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, eventId);
            ResultSet rs = ste.executeQuery();

            int participantCount = 0;
            while (rs.next()) {
                String email = rs.getString("email");
                if (email != null && !email.isEmpty()) {
                    System.out.println("Sending email to: " + email);
                    try {
                        // Send email to each participant
                        EmailUtil.sendEmail(email, subject, body);
                        System.out.println("Email sent successfully to: " + email);
                        participantCount++;
                    } catch (Exception e) {
                        System.err.println("Failed to send email to " + email + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid or empty email address for participant.");
                }
            }

            if (participantCount == 0) {
                System.out.println("No participants found for event ID: " + eventId);
            } else {
                System.out.println("Emails sent to " + participantCount + " participants.");
            }
        } catch (SQLException e) {
            System.err.println("SQL error while fetching participants: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception to handle it in the calling method
        }
    }

    public void supprimer(int id) throws SQLException {
        // Fetch event details before deleting
        evenement event = getEventById(id);
        if (event == null) {
            throw new SQLException("Événement non trouvé.");
        }

        String sql = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id);
            ste.executeUpdate();
            System.out.println("Evenement supprimé");

            // Notify participants
            notifyParticipants(id, "Annulation de l'événement", "L'événement '" + event.getTitre() + "' a été annulé. Nous sommes désolés pour tout inconvénient.");
        }
    }

    //    @Override
//    public void supprimer(int id) throws SQLException {
//        String sql = "delete from evenement where id=?";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setInt(1, id);
//        ste.executeUpdate();
//        System.out.println("Evenement supprimé");
//    }
//private void notifyParticipants(int eventId, String message) throws SQLException {
//    String sql = "SELECT p.telephone FROM participants p " +
//            "JOIN participation pt ON p.id = pt.user_id " +
//            "WHERE pt.evenement_id = ?";
//    try (PreparedStatement ste = cnx.prepareStatement(sql)) {
//        ste.setInt(1, eventId);
//        ResultSet rs = ste.executeQuery();
//
//        while (rs.next()) {
//            String phoneNumber = rs.getString("telephone");
//            if (phoneNumber != null && !phoneNumber.isEmpty()) {
//                System.out.println("Sending SMS to: " + phoneNumber);
//                TwilioSMS.sendSMS(phoneNumber, message);
//            } else {
//                System.out.println("No phone number found for participant.");
//            }
//        }
//    }
//}

    public void modifier(int id, String titre, String type, String description, Date date, String lieu, int nbrplaces, int sponsor) throws SQLException {
        String sql = "UPDATE evenement SET titre=?, type=?, description=?, date=?, lieu=?, nbrplaces=?, sponsor=? WHERE id=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, titre);
            ste.setString(2, type);
            ste.setString(3, description);
            ste.setDate(4, new Date(date.getTime()));
            ste.setString(5, lieu);
            ste.setInt(6, nbrplaces);
            ste.setInt(7, sponsor);
            ste.setInt(8, id);
            ste.executeUpdate();
            System.out.println("Evenement modifié");

            // Notify participants
            notifyParticipants(id, "Mise à jour de l'événement", "L'événement '" + titre + "' a été mis à jour. Nouvelle date: " + date);
        }
    }


    @Override
    public List<evenement> recuperer() throws SQLException {
        String sql = "select * from evenement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<evenement> evenements = new ArrayList<>();
        while (rs.next()) {
            String typeStr = rs.getString("type");
            type_evenement type;
            try {
                type = type_evenement.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown type: " + typeStr);
                // Handle the error, e.g., set a default value or throw a custom exception
                type = type_evenement.conference; // Default value
            }
            evenement e = new evenement(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    type,
                    rs.getString("description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("lieu"),
                    rs.getInt("nbrplaces"),
                    rs.getInt("sponsor"));
            evenements.add(e);
        }
        return evenements;
    }
//    @Override
//    public List<evenement> recuperer() throws SQLException {
//        String sql = "select * from evenement";
//        Statement ste = cnx.createStatement();
//        ResultSet rs = ste.executeQuery(sql);
//        List<evenement> evenements = new ArrayList<>();
//        while (rs.next()) {
//            evenement e = new evenement(
//                    rs.getInt("id"),
//                    rs.getString("titre"),
//                    type_evenement.valueOf(rs.getString("type")),
//                    rs.getString("description"),
//                    rs.getDate("date").toLocalDate(),
//                    rs.getString("lieu"),
//                    rs.getInt("nbrplaces"),
//                    rs.getInt("sponsor"));
//            evenements.add(e);
//        }
//        return evenements;
//    }


//    public List<evenement> getAllEvenements() {
//        List<evenement> list = new ArrayList<>();
//        String query = "SELECT * FROM evenement";
//
//        try (Statement stmt = cnx.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                evenement e = new evenement(
//                        rs.getInt("id"),
//                        rs.getString("titre"),
//                        type_evenement.valueOf(rs.getString("type")),
//                        rs.getString("description"),
//                        rs.getDate("date").toLocalDate(),
//                        rs.getString("lieu"),
//                        rs.getInt("nbrPlaces"),
//                        rs.getInt("sponsor")
//                );
//                list.add(e);
//            }
//            System.out.println("✅ Récupération des événements réussie !");
//        } catch (SQLException e) {
//            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
//        }
//        return list;
//    }

    public List<evenement> getAll() throws SQLException {
        List<evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";

        try (
                Statement stmt = cnx.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                evenement e = new evenement(
                        rs.getString("titre"),
                        type_evenement.valueOf(rs.getString("type")),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getInt("nbr_places"),
                        rs.getInt("sponsor")
                );
                evenements.add(e);
            }
        }
        return evenements;
    }

    public evenement getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, eventId);
            ResultSet rs = ste.executeQuery();
            if (rs.next()) {
                String typeStr = rs.getString("type");
                type_evenement type;
                try {
                    type = type_evenement.valueOf(typeStr); // Convert string to enum
                } catch (IllegalArgumentException e) {
                    // Handle unknown enum values
                    System.err.println("Unknown type: " + typeStr);
                    type = type_evenement.conference; // Default value or handle as needed
                }

                return new evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        type, // Use the resolved enum value
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getInt("nbrplaces"),
                        rs.getInt("sponsor")
                );
            }
        }
        return null; // Return null if no event is found
    }



//    public evenement getEventById(int eventId) throws SQLException {
//        String sql = "SELECT * FROM evenement WHERE id = ?";
//        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
//            ste.setInt(1, eventId);
//            ResultSet rs = ste.executeQuery();
//            if (rs.next()) {
//                return new evenement(
//                        rs.getInt("id"),
//                        rs.getString("titre"),
//                        models.type_evenement.valueOf(rs.getString("type")),
//                        rs.getString("description"),
//                        rs.getDate("date").toLocalDate(),
//                        rs.getString("lieu"),
//                        rs.getInt("nbrplaces"),
//                        rs.getInt("sponsor")
//                );
//            }
//        }
//        return null;
//    }

}