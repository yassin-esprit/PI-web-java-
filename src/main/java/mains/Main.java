package mains;

import models.statut_pub;
import models.type_pub;
import tools.MyDB;
import models.publicites;
import services.ServicePublicite;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Test database connection
        MyDB dbInstance = MyDB.getInstance();
        if (dbInstance.getConnection() != null) {
            System.out.println("Database connection is working!");
        } else {
            System.out.println("Failed to connect to the database.");
        }

        publicites p = new publicites(1, "hola" , type_pub.video , "je suis la " , "tunis" , statut_pub.active );

        ServicePublicite sv = new ServicePublicite();
        try {
//            if (p.getType_pub() == null) {
//                throw new IllegalArgumentException("Type de publicité ne peut pas être null");
//            }
//            if (p.getStatut_pub() == null) {
//                throw new IllegalArgumentException("Statut de publicité ne peut pas être null");
//            }
            //sv.ajouter(p);
            //sv.modifier(1, "holix" , type_pub.VIDEO , "je suis la " , "tunis" , statut_pub.ACTIVE);
            System.out.println(sv.recuperer());
           // sv.supprimer(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}