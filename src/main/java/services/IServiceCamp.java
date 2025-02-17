package services;

import models.campagnes_publicitaires;
import models.statut_camp;
import models.statut_pub;
import models.type_pub;

import java.sql.SQLException;
import java.util.List;



public interface IServiceCamp<T>
{
    void ajouter(T c) throws SQLException;

    void supprimer(int id_camp) throws SQLException;


   void modifier(int id_camp, String nom_camp, int budget_camp, String date_debut_camp, String date_fin_camp, statut_camp statut_camp, int publicite_id) throws SQLException;

   List<T> recuperer() throws SQLException;
}
