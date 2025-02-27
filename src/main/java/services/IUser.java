package Services;

import Models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUser<T>{


    void ajouter(User p) throws SQLException;

    void ajouter(T p) throws SQLException;
        void supprimer(int id) throws SQLException;
        void modifier(int id,String nom,int age) throws SQLException;
        List<T> recuperer() throws SQLException;



}
