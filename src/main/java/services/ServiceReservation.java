package services;

import models.Status_reservation;
import services.IServicesReservation;
import tools.MyDB;
import models.Reservation_Equipement;
import models.Equipement;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.evenement;
import models.evenement;


public class ServiceReservation implements IServicesReservation<Reservation_Equipement> {
    private Connection cnx;

    public ServiceReservation() {
        cnx = MyDB.getInstance().getConnection();
    }

    public boolean equipementExiste(int idEquipement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM equipement WHERE id_eq = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEquipement);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public boolean evenementExiste(int idEvenement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idEvenement);
        ResultSet rs = ste.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }
    public List<Equipement> recupererEquipements() throws SQLException {
        String sql = "SELECT * FROM equipement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Equipement> equipements = new ArrayList<>();
        while (rs.next()) {
            Equipement equipement = new Equipement();
            equipement.setId_eq(rs.getInt("id_eq"));
            equipement.setNom(rs.getString("nom"));
            equipements.add(equipement);
        }
        return equipements;
    }
    public Equipement getEquipementParNom(String nomEquipement) throws SQLException {
        String sql = "SELECT * FROM equipement WHERE nom = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, nomEquipement);
        ResultSet rs = ste.executeQuery();

        if (rs.next()) {
            Equipement equipement = new Equipement();
            equipement.setId_eq(rs.getInt("id_eq"));
            equipement.setNom(rs.getString("nom"));
            return equipement;
        }
        return null;
    }
//    public evenement getEvenementParNom(String TitreEvenement) throws SQLException {
//        String sql = "SELECT * FROM evenement WHERE titre = ?";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setString(1, TitreEvenement);
//        ResultSet rs = ste.executeQuery();
//
//        if (rs.next()) {
//            evenement evenement = new evenement();
//            evenement.setId(rs.getInt("id"));
//            evenement.setTitre(rs.getString("titre"));
//            return evenement;
//        }
//        return null;
//    }
public evenement getEvenementParNom(String titreEvenement) throws SQLException {
    String sql = "SELECT * FROM evenement WHERE titre = ?";
    PreparedStatement ste = cnx.prepareStatement(sql);
    ste.setString(1, titreEvenement);
    ResultSet rs = ste.executeQuery();

    if (rs.next()) {
        evenement evenement = new evenement();
        evenement.setId(rs.getInt("id"));
        evenement.setTitre(rs.getString("titre"));
        return evenement;
    }
    return null;
}
    public List<Equipement> LesEquipements() throws SQLException {
        List<Equipement> equipements = new ArrayList<>();
        String sql = "SELECT * FROM equipement"; // Requête pour récupérer tous les équipements
        PreparedStatement ste = cnx.prepareStatement(sql);
        ResultSet rs = ste.executeQuery();

        while (rs.next()) {
            Equipement equipement = new Equipement();
            equipement.setId_eq(rs.getInt("id_eq"));
            equipement.setNom(rs.getString("nom"));
            equipements.add(equipement);
        }
        return equipements;
    }


    // Récupérer tous les événements
    public List<evenement> recupererEvenements() throws SQLException {
        String sql = "SELECT * FROM evenement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<evenement> evenements = new ArrayList<>();
        while (rs.next()) {
            evenement ev = new evenement();
            ev.setId(rs.getInt("id"));
            ev.setTitre(rs.getString("titre"));
            evenements.add(ev);
        }
        return evenements;
    }
//
public void ajouter(Reservation_Equipement R) throws SQLException {
    if (R.getEquipement() == null || R.getEvenement() == null) {
        throw new IllegalArgumentException("L'équipement ou l'événement ne peut pas être null.");
    }

    // Vérifier si l'équipement existe
    if (!equipementExiste(R.getEquipement().getId_eq())) {
        throw new SQLException("L'équipement n'existe pas.");
    }

    // Vérifier si l'événement existe
    if (!evenementExiste(R.getEvenement().getId())) {
        throw new SQLException("L'événement n'existe pas.");
    }

    String sql = "INSERT INTO reservation_equipement (id_eq, id_event, date_reservation, statut) " +
            "VALUES (?, ?, ?, ?)";
    PreparedStatement ste = cnx.prepareStatement(sql);
    ste.setInt(1, R.getEquipement().getId_eq());
    ste.setInt(2, R.getEvenement().getId());
    ste.setString(3, R.getDateReservation());
    ste.setString(4, R.getStatus_reservation().name());

    int rowsInserted = ste.executeUpdate();

    if (rowsInserted > 0) {
        System.out.println("Réservation ajoutée avec succès.");
    } else {
        System.out.println("Erreur : La réservation n'a pas pu être ajoutée.");
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
    public void modifier(int id_reservation, Equipement equipement, evenement evenement, String dateReservation, Status_reservation status_reservation) throws SQLException {
        if (!equipementExiste(equipement.getId_eq())) {
            System.out.println("Erreur : L'équipement n'existe pas.");
            return;
        }
        if (!evenementExiste(evenement.getId())) {
            System.out.println("Erreur : L'événement n'existe pas.");
            return;
        }

        String sql = "UPDATE reservation_equipement SET id_eq = ?, id_event = ?, date_reservation = ?, statut = ? WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, equipement.getId_eq());
        ste.setInt(2, evenement.getId());
        ste.setString(3, dateReservation);
        ste.setString(4, status_reservation.name());
        ste.setInt(5, id_reservation);
        ste.executeUpdate();
        System.out.println("Réservation modifiée.");
    }

    @Override
    public List<Reservation_Equipement> recuperer() throws SQLException {
        String sql = "SELECT r.id, e.nom AS nom_equipement, ev.titre AS titre_evenement, r.date_reservation, r.statut " +
                "FROM reservation_equipement r " +
                "JOIN equipement e ON r.id_eq = e.id_eq " +
                "JOIN evenement ev ON r.id_event = ev.id";

        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {

            List<Reservation_Equipement> reservations = new ArrayList<>();
            while (rs.next()) {
                Equipement equipement = new Equipement();
                evenement evenement = new evenement();
                evenement.setTitre(rs.getString("titre_evenement"));

                equipement.setNom(rs.getString("nom_equipement"));
                Reservation_Equipement R = new Reservation_Equipement();
                R.setId_reservation(rs.getInt("id"));
                R.setEquipement(equipement);
                R.setEvenement(evenement);
                R.setDateReservation(rs.getString("date_reservation"));
                R.setStatus_reservation(Status_reservation.valueOf(rs.getString("statut").toUpperCase()));

                reservations.add(R);
            }
            return reservations;
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            throw e;
        }
    }
//    public List<Reservation_Equipement> recuperer() throws SQLException {
//        String sql = "SELECT r.id, e.nom AS nom_equipement, ev.titre AS titre_evenement, r.date_reservation, r.statut " +
//                "FROM reservation_equipement r " +
//                "JOIN equipement e ON r.id_eq = e.id_eq " +
//                "JOIN evenement ev ON r.id_event = ev.id";
//
//        Statement ste = cnx.createStatement();
//        ResultSet rs = ste.executeQuery(sql);
//
//        List<Reservation_Equipement> reservations = new ArrayList<>();
//        while (rs.next()) {
//            Equipement equipement = new Equipement();
//            evenement evenement= new evenement();
//            evenement.setTitre(rs.getString("titre"));
//
//            equipement.setNom(rs.getString("nom_equipement"));
//            Reservation_Equipement R = new Reservation_Equipement();
//            R.setId_reservation(rs.getInt("id"));
//            R.setEquipement(equipement);
//            R.setEvenement(evenement);
//            //R.setTitre(rs.getString("titre_evenement"));
//            R.setDateReservation(rs.getString("date_reservation"));
//            R.setStatus_reservation(Status_reservation.valueOf(rs.getString("statut").toUpperCase()));
//
//            reservations.add(R);
//        }
//        return reservations;
//    }
}