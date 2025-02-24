package models;
import models.Equipement;
public class Reservation_Equipement {
    private int id_reservation;
   // private int id_equipement;
    private Equipement equipement;
    private evenement evenement;
   // private int id_evenement;
    private String dateReservation;
    private Status_reservation status_reservation;
    public Reservation_Equipement() {}

    public Reservation_Equipement(int id_reservation, Equipement equipement, evenement evenement, String dateReservation, Status_reservation status_reservation) {
        this.id_reservation = id_reservation;
        this.dateReservation = dateReservation;
        this.equipement = equipement;
        this.status_reservation = status_reservation;
        this.evenement = evenement;
    }

    public Reservation_Equipement(evenement evenement, Equipement equipement, String dateReservation, Status_reservation status_reservation) {
        this.evenement = evenement;
        this.equipement = equipement;
        this.dateReservation = dateReservation;
        this.status_reservation = status_reservation;
    }

    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

    public Status_reservation getStatus_reservation() {
        return status_reservation;
    }

    public void setStatus_reservation(Status_reservation status_reservation) {
        this.status_reservation = status_reservation;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }



    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public models.evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(models.evenement evenement) {
        this.evenement = evenement;
    }

    @Override
    public String toString() {
        return "Reservation_Equipement{" +
                "id_reservation=" + id_reservation +
                ", equipement=" + equipement +
                ", id_evenement=" + evenement +
                ", dateReservation=" + dateReservation +
                ", status_reservation=" + status_reservation +
                '}';
    }
}
