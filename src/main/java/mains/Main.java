package mains;
import models.Equipement;
import services.ServiceEquipement;
import tools.MyDB;
import models.Status_Equipement;
import services.ServiceReservation;
import models.Status_reservation;
import models.Reservation_Equipement;
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
        Reservation_Equipement R1 = new Reservation_Equipement( 1, 2, 1,"2025-02-01",  Status_reservation.CONFIRME);
        Reservation_Equipement R2 = new Reservation_Equipement( 2, 2, 1, "2026-02-01",  Status_reservation.ANNULE);
        Equipement e1 = new Equipement(1,"bafle","top",20,10,Status_Equipement.dispo);
        Equipement e2 = new Equipement(2,"lumiere","geante",10,5,Status_Equipement.dispo);
        ServiceReservation sr = new ServiceReservation();
        ServiceEquipement se =new ServiceEquipement();
        try {
           // sr.ajouter(R2);
            //sr.ajouter(R2);
            //sr.modifier(1, 2, 1, "2028-02-22",  Status_reservation.annule);
            //sr.supprimer(1);
            System.out.println(sr.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        try {
             //se.ajouter(e2);
              //se.modifier(2,"baaafle","toop",20,5,Status_Equipement.indisponible);
            //se.supprimer(1);
            System.out.println(se.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
