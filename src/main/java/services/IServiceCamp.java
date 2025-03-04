package services;

import models.*;

import java.sql.SQLException;
import java.util.List;



public interface IServiceCamp<T>
{
    void ajouter(T c) throws SQLException;

    void supprimer(int id_camp) throws SQLException;


   void modifier(int id_camp, String nom_camp, int budget_camp, String date_debut_camp, String date_fin_camp, statut_camp statut_camp, publicites publicite) throws SQLException;

   List<T> recuperer() throws SQLException;
}
