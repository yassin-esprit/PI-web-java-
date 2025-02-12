package models;

public class publicites {
    private int id_pub;
    private String titre_pub;
    private type_pub type_pub;
    private String contenu_pub;
    private String emplacement_pub;
    private statut_pub statut_pub;

    public publicites() {
    }

    public publicites(String titre_pub , type_pub type_pub , String contenu_pub , String emplacement_pub, statut_pub statut_pub) {
        this.titre_pub = titre_pub;
        this.type_pub = type_pub;
        this.contenu_pub = contenu_pub;
        this.emplacement_pub = emplacement_pub;
        this.statut_pub = statut_pub;

    }

    public publicites(int id_pub,String titre_pub , type_pub type_pub , String contenu_pub , String emplacement_pub, statut_pub statut_pub) {
        this.id_pub = id_pub;
        this.titre_pub = titre_pub;
        this.type_pub = type_pub;
        this.contenu_pub = contenu_pub;
        this.emplacement_pub = emplacement_pub;
        this.statut_pub = statut_pub;
    }

    public int getId_pub() {
        return id_pub;
    }

    public void setId_pub(int id_pub) {
        this.id_pub = id_pub;
    }

    public String getTitre_pub() {
        return titre_pub;
    }

    public void setTitre_pub(String titre_pub) {
        this.titre_pub = titre_pub;
    }

    public type_pub getType_pub() {
        return type_pub;
    }

    public void setType_pub(type_pub type_pub) {
        this.type_pub = type_pub;
    }

    public String getContenu_pub() {
        return contenu_pub;
    }

    public void setContenu_pub(String contenu_pub) {
        this.contenu_pub = contenu_pub;
    }

    public String getEmplacement_pub() {
        return emplacement_pub;
    }

    public void setEmplacement_pub(String emplacement_pub) {
        this.emplacement_pub = emplacement_pub;
    }

    public statut_pub getStatut_pub() {
        return statut_pub;
    }

    public void setStatut_pub(statut_pub statut_pub) {
        this.statut_pub = statut_pub;
    }

    @Override
    public String toString() {
        return "publicites{" +
                "id_pub=" + id_pub +
                ", titre_pub='" + titre_pub + '\'' +
                ", type_pub=" + type_pub +
                ", contenu_pub='" + contenu_pub + '\'' +
                ", emplacement_pub='" + emplacement_pub + '\'' +
                ", statut_pub=" + statut_pub +
                '}';
    }
}
