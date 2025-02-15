package services;

import models.Equipement;
import models.Status_Equipement;
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
    private boolean equipementExiste(int idEquipement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM equipement WHERE id_eq = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEquipement);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0; // Retourne true si l'équipement existe
    }

    // Méthode pour vérifier si l'événement existe
    private boolean evenementExiste(int idEvenement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEvenement);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0; // Retourne true si l'événement existe
    }

    @Override
    public void ajouter(Reservation_Equipement R) throws SQLException {
        // Vérification si l'équipement et l'événement existent
        if (!equipementExiste(R.getId_equipement())) {
            System.out.println("Erreur : L'équipement n'existe pas.");
            return;
        }
        if (!evenementExiste(R.getId_evenement())) {
            System.out.println("Erreur : L'événement n'existe pas.");
            return;
        }

        String sql = "INSERT INTO reservation_equipement (id, id_eq, id_event, date_reservation, statut) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, R.getId_reservation());
        ste.setInt(2, R.getId_equipement());
        ste.setInt(3, R.getId_evenement());
        ste.setString(4, R.getDateReservation());
        ste.setString(5, R.getStatus_reservation().name()); // Enum converti en String
        ste.executeUpdate();
        System.out.println("Reservation ajouté");
    }

    @Override
    public void supprimer(int id_reservation) throws SQLException {
        String sql = "DELETE FROM reservation_equipement WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id_reservation);
        ste.executeUpdate();
        System.out.println("Reservation supprimé");
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

        String sql = "UPDATE reservation_equipement SET id_eq = ?, id_event = ?, date_reservation = ?,  statut = ? WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id_equipement);
        ste.setInt(2, id_evenement);
        ste.setString(3, dateReservation);
        ste.setString(4, status_reservation.name()); // Conversion enum en String
        ste.setInt(5, id_reservation);
        ste.executeUpdate();
        System.out.println("Reservation modifié");
    }

    @Override
    public List<Reservation_Equipement> recuperer() throws SQLException {
        String sql = "SELECT * FROM reservation_equipement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Reservation_Equipement> Reservations = new ArrayList<>();
        while (rs.next()) {
            Reservation_Equipement R = new Reservation_Equipement();
            R.setId_equipement(rs.getInt("id"));
            R.setId_equipement(rs.getInt("id_eq"));
            R.setId_evenement(rs.getInt("id_event"));
            R.setDateReservation(rs.getString("date_reservation"));
            R.setStatus_reservation(Status_reservation.valueOf(rs.getString("statut")));

            Reservations.add(R);
        }
        return Reservations;
    }
}
