package mains;

import java.sql.Date;
import java.sql.SQLException;

import models.evenement;
import services.ServiceEvenement;

public class Main {
    public static void main(String[] args) {
        evenement ev = new evenement("titre1", "type1", "description1", new Date(2021, 12, 12), "lieu1", 100,
                1);
        ServiceEvenement se = new ServiceEvenement();

        try {
            se.ajouter(ev);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        //

    }

}
