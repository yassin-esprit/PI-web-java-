package services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IServices<T> {
    void ajouter(T p) throws SQLException;

    void supprimer(int id) throws SQLException;

    void modifier(int id, String titre, String type, String description, Date date, String lieu, int nbrplaces,
            int sponsor) throws SQLException;

    List<T> recuperer() throws SQLException;

}
