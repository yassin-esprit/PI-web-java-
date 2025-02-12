package mains;
import models.Equipement;
import services.ServiceEquipement;
import tools.MyDB;
import models.Status_Equipement;import java.sql.SQLException;
public class Main {
    public static void main(String[] args) {
        // Test database connection
        MyDB dbInstance = MyDB.getInstance();
        if (dbInstance.getConnection() != null) {
            System.out.println("Database connection is working!");
        } else {
            System.out.println("Failed to connect to the database.");
        }
        Equipement e1 = new Equipement(1,"bafle","top",20,10,Status_Equipement.dispo);
        Equipement e2 = new Equipement(2,"lumiere","geante",10,5,Status_Equipement.dispo);

        ServiceEquipement se =new ServiceEquipement();
        try {
             //se.ajouter(e2);
              se.modifier(2,"baaafle","toop",20,5,Status_Equipement.indisponible);
            se.supprimer(1);
            System.out.println(se.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
