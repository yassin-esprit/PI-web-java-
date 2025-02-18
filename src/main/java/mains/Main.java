package mains;

import models.Personne;
import services.ServicePersonne;
import tools.MyDB;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Personne p = new Personne(30,"mezri","aziz");
        ServicePersonne sv = new ServicePersonne();
        try {
           // sv.ajouter(p);
          //  sv.modifier(2,"ben ahmed",33);
            System.out.println(sv.recuperer());
        //sv.supprimer(1);
      } catch (SQLException e) {
          System.err.println(e.getMessage());
       }



    }

}
