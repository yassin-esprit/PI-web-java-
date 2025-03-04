package models;

public class Evenement {
    private int id;
    private String titre;
    private String type;
    private String date;
    private String lieu;
    private int nbrPlaces;
    private int sponsor;

    // Default constructor
    public Evenement() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getNbrPlaces() { return nbrPlaces; }
    public void setNbrPlaces(int nbrPlaces) { this.nbrPlaces = nbrPlaces; }

    public int getSponsor() { return sponsor; }
    public void setSponsor(int sponsor) { this.sponsor = sponsor; }
    @Override
    public String toString() {
        return titre != null && !titre.isEmpty() ? titre : "Événement sans titre" +
                " [ID: " + id +
                ", Type: " + (type != null ? type : "N/A") +
                ", Date: " + (date != null ? date : "N/A") +
                ", Lieu: " + (lieu != null ? lieu : "N/A") +
                ", Places: " + nbrPlaces +
                ", Sponsor: " + sponsor + "]";
    }
}