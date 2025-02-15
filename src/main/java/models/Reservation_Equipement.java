package models;

public class Reservation_Equipement {
    private int id_reservation;
    private int id_equipement;
    private int id_evenement;
    private String dateReservation;
    private Status_reservation status_reservation;
    public Reservation_Equipement() {}

    public Reservation_Equipement(int id_reservation, int id_equipement, int id_evenement, String dateReservation, Status_reservation status_reservation) {
        this.id_reservation = id_reservation;
        this.id_equipement = id_equipement;
        this.id_evenement = id_evenement;
        this.dateReservation = dateReservation;
        this.status_reservation = status_reservation;
    }

    public Reservation_Equipement(int id_evenement, int id_equipement, String dateReservation, Status_reservation status_reservation) {
        this.id_evenement = id_evenement;
        this.id_equipement = id_equipement;
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

    public int getId_evenement() {
        return id_evenement;
    }

    public void setId_evenement(int id_evenement) {
        this.id_evenement = id_evenement;
    }

    public int getId_equipement() {
        return id_equipement;
    }

    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    @Override
    public String toString() {
        return "Reservation_Equipement{" +
                "id_reservation=" + id_reservation +
                ", id_equipement=" + id_equipement +
                ", id_evenement=" + id_evenement +
                ", dateReservation=" + dateReservation +
                ", status_reservation=" + status_reservation +
                '}';
    }
}
