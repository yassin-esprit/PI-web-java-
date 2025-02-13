package mains;

import java.sql.Date;
import java.sql.SQLException;
import models.evenement;
import models.type_evenement;
import services.ServiceEvenement;

public class Main {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        evenement ev = new evenement("titre1", type_evenement.conference, "description1", new Date(2021, 12, 12),
                "lieu1", 100,
                1);
        ServiceEvenement se = new ServiceEvenement();

        try {
            se.ajouter(ev);
            // se.supprimer(3);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

}
