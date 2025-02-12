package models;

import java.util.Date;

public class evenement {
    private int id;
    private String titre;
    private String type;
    private String description;
    private Date date;
    private String lieu;
    private int nbrplaces;
    private int sponsor;

    public evenement(String titre, String type, String description, Date date, String lieu, int nbrplaces,
            int sponsor) {
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.nbrplaces = nbrplaces;
        this.sponsor = sponsor;
    }

    public evenement(int id, String titre, String type, String description, Date date, String lieu, int nbrplaces,
            int sponsor) {
        this.id = id;
        this.titre = titre;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return "Evenement [id=" + id + ", titre=" + titre + ", type=" + type + ", description=" + description
                + ", date=" + date + ", lieu=" + lieu + ", nbrplaces=" + nbrplaces + ", sponsor=" + sponsor + "]";
    }
}