package services;

import models.Equipement;
import models.Status_Equipement;
import tools.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipement implements IServices<Equipement> {

    private Connection cnx;

    public ServiceEquipement() {
        cnx = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Equipement eq) throws SQLException {
        String sql = "INSERT INTO equipement (nom, quantite_totale, quantite_dispo, description, statut) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, eq.getNom());
        ste.setInt(2, eq.getQuantite_totale());
        ste.setInt(3, eq.getQuantite_disponible());
        ste.setString(4, eq.getDescription());
        ste.setString(5, eq.getStatut().name()); // Enum converti en String
        ste.executeUpdate();
        System.out.println("Équipement ajouté");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM equipement WHERE id_eq = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Équipement supprimé");
    }

    @Override
    public void modifier(int id, String nom, String description, int quantiteTotale, int quantiteDisponible, Status_Equipement statut) throws SQLException {
        String sql = "UPDATE equipement SET nom = ?, quantite_totale = ?, quantite_dispo = ?, description = ?, statut = ? WHERE id_eq = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, nom);
        ste.setInt(2, quantiteTotale);
        ste.setInt(3, quantiteDisponible);
        ste.setString(4, description);
        ste.setString(5, statut.name()); // Conversion enum en String
        ste.setInt(6, id);
        ste.executeUpdate();
        System.out.println("Équipement modifié");
    }

    @Override
    public List<Equipement> recuperer() throws SQLException {
        String sql = "SELECT * FROM equipement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Equipement> equipements = new ArrayList<>();
        while (rs.next()) {
            Equipement eq = new Equipement();
            eq.setId_eq(rs.getInt("id_eq"));
            eq.setNom(rs.getString("nom"));
            eq.setQuantite_totale(rs.getInt("quantite_totale"));
            eq.setQuantite_disponible(rs.getInt("quantite_dispo"));
            eq.setDescription(rs.getString("description"));
            eq.setStatut(Status_Equipement.valueOf(rs.getString("statut")));

            equipements.add(eq);
        }
        return equipements;
    }
}
