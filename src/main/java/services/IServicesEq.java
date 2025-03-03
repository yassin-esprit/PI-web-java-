package services;

import models.Status_Equipement;
import java.sql.SQLException;
import java.util.List;

public interface IServicesEq<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(int id, String nom, String description, int quantiteTotale, int quantiteDisponible, Status_Equipement statut) throws SQLException;
    List<T> recuperer() throws SQLException;
}
