package services;

import models.Personne;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T> {
    void ajouter(T p) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(int id,String nom,int age) throws SQLException;
    List<T> recuperer() throws SQLException;

}
