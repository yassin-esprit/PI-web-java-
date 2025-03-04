package services;

import models.statut_pub;
import models.type_pub;

import java.sql.SQLException;
import java.util.List;

public interface IServicesP<T> {

    void ajouter(T p) throws SQLException;
    void supprimer (int id_pub)throws SQLException;

    void modifier(int id_pub, String titre_pub, type_pub type_pub, String contenu_pub, String emplacement_pub, statut_pub statut_pub) throws SQLException;

    List<T> recuperer() throws SQLException;
}



