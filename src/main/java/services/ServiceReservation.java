package services;

import models.Status_reservation;
import tools.MyDB;
import models.Reservation_Equipement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IServicesReservation<Reservation_Equipement> {
    private Connection cnx;

    public ServiceReservation() {
        cnx = MyDB.getInstance().getConnection();
    }

    // Méthode pour vérifier si l'équipement existe
    public boolean equipementExiste(int idEquipement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM equipement WHERE id_eq = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEquipement);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0; // Retourne true si l'équipement existe
    }

    // Méthode pour récupérer les ID des événements
    public List<Integer> LesIdEvenements() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM evenement";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ResultSet rs = ste.executeQuery();

        while (rs.next()) {
            ids.add(rs.getInt("id"));
        }
        return ids;
    }

    // Méthode pour récupérer les ID des équipements
    public List<Integer> LesIdEquipements() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id_eq FROM equipement";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ResultSet rs = ste.executeQuery();

        while (rs.next()) {
            ids.add(rs.getInt("id_eq"));
        }
        return ids;
    }

    // Méthode pour vérifier si l'événement existe
    public boolean evenementExiste(int idEvenement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEvenement);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0; // Retourne true si l'événement existe
    }

    @Override
    public void ajouter(Reservation_Equipement R) throws SQLException {
        String sql = "INSERT INTO reservation_equipement (id_eq, id_event, date_reservation, statut) " +
                "SELECT e.id_eq, ev.id, ?, ? " +
                "FROM equipement e " +
                "INNER JOIN evenement ev ON ev.id = ? " +
                "WHERE e.id_eq = ?";

        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, R.getDateReservation());
        ste.setString(2, R.getStatus_reservation().name()); // Enum converti en String
        ste.setInt(3, R.getId_evenement());
        ste.setInt(4, R.getId_equipement());

        int rowsInserted = ste.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Réservation ajoutée avec succès.");
        } else {
            System.out.println("Erreur : L'équipement ou l'événement n'existe pas.");
        }
    }

    @Override
    public void supprimer(int id_reservation) throws SQLException {
        String sql = "DELETE FROM reservation_equipement WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id_reservation);
        ste.executeUpdate();
        System.out.println("Réservation supprimée.");
    }

    @Override
    public void modifier(int id_reservation, int id_equipement, int id_evenement, String dateReservation, Status_reservation status_reservation) throws SQLException {
        // Vérification si l'équipement et l'événement existent
        if (!equipementExiste(id_equipement)) {
            System.out.println("Erreur : L'équipement n'existe pas.");
            return;
        }
        if (!evenementExiste(id_evenement)) {
            System.out.println("Erreur : L'événement n'existe pas.");
            return;
        }

        String sql = "UPDATE reservation_equipement SET id_eq = ?, id_event = ?, date_reservation = ?, statut = ? WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id_equipement);
        ste.setInt(2, id_evenement);
        ste.setString(3, dateReservation);
        ste.setString(4, status_reservation.name()); // Conversion enum en String
        ste.setInt(5, id_reservation);
        ste.executeUpdate();
        System.out.println("Réservation modifiée.");
    }

    @Override
    public List<Reservation_Equipement> recuperer() throws SQLException {
        String sql = "SELECT * FROM reservation_equipement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Reservation_Equipement> Reservations = new ArrayList<>();
        while (rs.next()) {
            Reservation_Equipement R = new Reservation_Equipement();
            R.setId_reservation(rs.getInt("id"));
            R.setId_equipement(rs.getInt("id_eq"));
            R.setId_evenement(rs.getInt("id_event"));
            R.setDateReservation(rs.getString("date_reservation"));

            try {
                String statut = rs.getString("statut");
                R.setStatus_reservation(Status_reservation.valueOf(statut.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : Statut invalide pour la réservation (id=" + R.getId_reservation() + ")");
            }

            Reservations.add(R);
        }
        return Reservations;
    }
}
