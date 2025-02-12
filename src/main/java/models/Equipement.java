package models;

public class Equipement {
    private int id_eq;
    private String nom;
    private String description;
    private int quantite_totale;
    private int quantite_disponible;
    private Status_Equipement statut;

    public Equipement() {}

    public Equipement(String nom, String description, int quantite_disponible, int quantite_totale, Status_Equipement statut) {
        this.nom = nom;
        this.description = description;
        this.quantite_disponible = quantite_disponible;
        this.quantite_totale = quantite_totale;
        this.statut = statut; // Correction ici
    }

    public Equipement(int id_eq, String nom, String description, int quantite_disponible, int quantite_totale, Status_Equipement statut) {
        this.id_eq = id_eq;
        this.nom = nom;
        this.description = description;
        this.quantite_disponible = quantite_disponible;
        this.quantite_totale = quantite_totale;
        this.statut = statut;
    }

    // Getters et Setters
    public int getId_eq() { return id_eq; }
    public void setId_eq(int id_eq) { this.id_eq = id_eq; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getQuantite_totale() { return quantite_totale; }
    public void setQuantite_totale(int quantite_totale) { this.quantite_totale = quantite_totale; }
    public int getQuantite_disponible() { return quantite_disponible; }
    public void setQuantite_disponible(int quantite_disponible) { this.quantite_disponible = quantite_disponible; }
    public Status_Equipement getStatut() { return statut; }
    public void setStatut(Status_Equipement statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Equipement{" +
                "id_eq=" + id_eq +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", quantite_totale=" + quantite_totale +
                ", quantite_disponible=" + quantite_disponible +
                ", statut=" + statut +
                '}';
    }
}
