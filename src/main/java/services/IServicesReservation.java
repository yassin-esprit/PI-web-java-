package services;

import models.Status_Equipement;
import models.Status_reservation;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IServicesReservation<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(int id_reservation) throws SQLException;
    void modifier(int id_reservation, int id_equipement, int id_evenement, String dateReservation, Status_reservation status_reservation) throws SQLException;
    List<T> recuperer() throws SQLException;
    boolean equipementExiste(int idEquipement) throws SQLException;
}
