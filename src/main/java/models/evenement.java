package models;

// import models.type_evenement;
import java.util.Date;

public class evenement {
    private int id;
    private String titre;
    private type_evenement type_evenement;
    private String description;
    private Date date;
    private String lieu;
    private int nbrplaces;
    private int sponsor;

    public evenement(String titre, type_evenement type_evenement, String description, Date date, String lieu,
            int nbrplaces,
            int sponsor) {
        this.titre = titre;
        this.type_evenement = type_evenement;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.nbrplaces = nbrplaces;
        this.sponsor = sponsor;
    }

    public evenement(int id, String titre, type_evenement type_evenement, String description, Date date, String lieu,
            int nbrplaces,
            int sponsor) {
        this.id = id;
        this.titre = titre;
        this.type_evenement = type_evenement;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.nbrplaces = nbrplaces;
        this.sponsor = sponsor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public type_evenement getType() {
        return type_evenement;
    }

    public void setType(type_evenement type) {
        this.type_evenement = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getNbrplaces() {
        return nbrplaces;
    }

    public void setNbrplaces(int nbrplaces) {
        this.nbrplaces = nbrplaces;
    }

    public int getSponsor() {
        return sponsor;
    }

    public void setSponsor(int sponsor) {
        this.sponsor = sponsor;
    }

    @Override
    public String toString() {
        return "Evenement [id=" + id + ", titre=" + titre + ", type=" + type_evenement + ", description=" + description
                + ", date=" + date + ", lieu=" + lieu + ", nbrplaces=" + nbrplaces + ", sponsor=" + sponsor + "]";
    }
}