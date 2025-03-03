package models;

public class conferencier {
    private int id_conferencier;
    private int id_evenement;
    private String nom;
    private String biographie;
    private String contact_email;

    // Constructor
    public conferencier(int id_conferencier, int id_evenement, String nom, String biographie, String contactEmail) {
        this.id_conferencier = id_conferencier;
        this.id_evenement = id_evenement;
        this.nom = nom;
        this.biographie = biographie;
        this.contact_email = contactEmail;
    }

    // Getters and Setters
    public int getIdConferencier() {
        return id_conferencier;
    }

    public void setIdConferencier(int idConferencier) {
        this.id_conferencier = idConferencier;
    }

    public int getIdEvenement() {
        return id_evenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.id_evenement = idEvenement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public String getContactEmail() {
        return contact_email;
    }

    public void setContactEmail(String contactEmail) {
        this.contact_email = contactEmail;
    }

    @Override
    public String toString() {
        return "Conferencier{" +
                "id_conferencier=" + id_conferencier +
                ", id_evenement=" + id_evenement +
                ", nom='" + nom + '\'' +
                ", biographie='" + biographie + '\'' +
                ", contact_email='" + contact_email + '\'' +
                '}';
    }
}