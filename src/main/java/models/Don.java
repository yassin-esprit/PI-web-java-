package models;

import java.util.Date;

public class Don {
    private int id;
    private int idEvenement;
    private double montant;
    private String typeDon;
    private String description;
    private String methodePaiement;
    private Date dateDon;

    // Constructor with idEvenement
    public Don(int idEvenement, double montant, String typeDon, String description,
               String methodePaiement, Date dateDon) {
        this.idEvenement = idEvenement;
        this.montant = montant;
        this.typeDon = typeDon;
        this.description = description;
        this.methodePaiement = methodePaiement;
        this.dateDon = dateDon;
    }

    // Constructor without idEvenement (for retrieval)
    public Don(double montant, String typeDon, String description,
               String methodePaiement, Date dateDon) {
        this.montant = montant;
        this.typeDon = typeDon;
        this.description = description;
        this.methodePaiement = methodePaiement;
        this.dateDon = dateDon;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEvenement() { return idEvenement; }
    public void setIdEvenement(int idEvenement) { this.idEvenement = idEvenement; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getTypeDon() { return typeDon; }
    public void setTypeDon(String typeDon) { this.typeDon = typeDon; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMethodePaiement() { return methodePaiement; }
    public void setMethodePaiement(String methodePaiement) { this.methodePaiement = methodePaiement; }

    public java.sql.Date getDateDon() { return new java.sql.Date(dateDon.getTime()); }
    public void setDateDon(Date dateDon) { this.dateDon = dateDon; }

    @Override
    public String toString() {
        return "Don{" +
                "id=" + id +
                ", idEvenement=" + idEvenement +
                ", montant=" + montant +
                ", typeDon='" + typeDon + '\'' +
                ", description='" + description + '\'' +
                ", methodePaiement='" + methodePaiement + '\'' +
                ", dateDon=" + dateDon +
                '}';
    }

    public String getTitreEvenement() {
        return String.valueOf(idEvenement);
    }
}