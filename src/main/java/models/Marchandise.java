package models;

public class Marchandise {
    private int id;
    private String nom;
    private byte[] image;  // Changed from String to byte[]
    private double prix;
    private String description;

    public Marchandise(String nom, byte[] image, double prix, String description) {
        this.nom = nom;
        this.image = image;
        this.prix = prix;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Marchandise{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", image=" + (image != null ? "Image present" : "No image") +
                ", prix=" + prix +
                ", description='" + description + '\'' +
                '}';
    }
}