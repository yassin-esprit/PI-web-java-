package services;

import models.Equipement;
import models.Reservation_Equipement;
import models.Status_Equipement;
import models.Status_reservation;
import models.evenement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IServicesReservation<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(int id_reservation) throws SQLException;
    public void modifier(int id_reservation, Equipement equipement, evenement evenement, String dateReservation, Status_reservation status_reservation) throws SQLException ;
    public List<Reservation_Equipement> recuperer() throws SQLException ;
    boolean equipementExiste(int idEquipement) throws SQLException;
}
