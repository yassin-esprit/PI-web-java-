package mains;

import services.ServiceCamp;
import services.ServicePublicite;
import tools.MyDB;


import java.sql.SQLException;


import models.campagnes_publicitaires;
import models.publicites;
import models.statut_camp;
import models.type_pub;
import models.statut_pub;


import java.sql.*;



public class Main {
    public static void main(String[] args) {
        // Test database connection
        MyDB dbInstance = MyDB.getInstance();
        if (dbInstance.getConnection() != null) {
            System.out.println("Database connection is working!");
        } else {
            System.out.println("Failed to connect to the database.");
        }
        //campagnes_publicitaires c1 = new campagnes_publicitaires("brsuno", 2000, "2026-02-01", "2026-02-15", statut_camp.suspendue, 1);

       // publicites p = new publicites(1, "hola" , type_pub.video , "je suis la " , "tunis" , statut_pub.active );
       // publicites p2 = new publicites(2, "lilas est la " , type_pub.image , "kim o " , "bardo" , statut_pub.inactif );

      //campagnes_publicitaires c2 = new campagnes_publicitaires("brsuno", 2000, "2026-02-01", "2026-02-15", statut_camp.suspendue, 62);
        //campagnes_publicitaires c3 = new campagnes_publicitaires("ELMA", 1500, "2025-02-01", "2025-02-20", statut_camp.en_cours, 26);

        ServicePublicite sv = new ServicePublicite();
        ServiceCamp sc = new ServiceCamp();

        try {
//            if (p.getType_pub() == null) {
//                throw new IllegalArgumentException("Type de publicité ne peut pas être null");
//            }
//            if (p.getStatut_pub() == null) {
//                throw new IllegalArgumentException("Statut de publicité ne peut pas être null");
//            }
           // sv.ajouter(p);
            //sv.ajouter(p2);
           // sc.ajouter(c1);
            //sv.modifier(1, "holix" , type_pub.VIDEO , "je suis la " , "tunis" , statut_pub.ACTIVE);
            System.out.println(sv.recuperer());
           // sv.supprimer(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        try {
           // sc.ajouter(c2);
            //sc.ajouter(c3);
           // sc.modifier(3,"ELMA",8000,"2025-02-01", "2025-02-20", statut_camp.suspendue,4);

          //  sc.supprimer(2);
           System.out.println(sc.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


}