package services;

import java.sql.SQLException;
import java.util.List;

public interface IServices_conferencier<T> {

    void ajouter(T p) throws SQLException;

    void supprimer(int id) throws SQLException;

    void modifier(int id_confiercier, int id_evenement, String nom, String biographie, String contact_email)
            throws SQLException;

    List<T> recuperer() throws SQLException;

}
