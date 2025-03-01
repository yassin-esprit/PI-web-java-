package models;

public class campagnes_publicitaires {

    private int id_camp;
    private String nom_camp;
    private int budget_camp;
    private String date_debut_camp;
    private String date_fin_camp;
    private statut_camp statut_camp;
    private publicites publicite;  // Référence à l'objet publicites

    // Constructeur par défaut
    public campagnes_publicitaires() {}

    // Constructeur sans id_camp
    public campagnes_publicitaires(String nom_camp, int budget_camp, String date_debut_camp, String date_fin_camp, statut_camp statut_camp, publicites publicite) {
        this.nom_camp = nom_camp;
        this.budget_camp = budget_camp;
        this.date_debut_camp = date_debut_camp;
        this.date_fin_camp = date_fin_camp;
        this.statut_camp = statut_camp;
        this.publicite = publicite;
    }

    // Constructeur avec id_camp
    public campagnes_publicitaires(int id_camp, String nom_camp, int budget_camp, String date_debut_camp, String date_fin_camp, statut_camp statut_camp, publicites publicite) {
        this.id_camp = id_camp;
        this.nom_camp = nom_camp;
        this.budget_camp = budget_camp;
        this.date_debut_camp = date_debut_camp;
        this.date_fin_camp = date_fin_camp;
        this.statut_camp = statut_camp;
        this.publicite = publicite;
    }

    // Getters et Setters
    public int getId_camp() {
        return id_camp;
    }

    public void setId_camp(int id_camp) {
        this.id_camp = id_camp;
    }

    public String getNom_camp() {
        return nom_camp;
    }

    public void setNom_camp(String nom_camp) {
        this.nom_camp = nom_camp;
    }

    public int getBudget_camp() {
        return budget_camp;
    }

    public void setBudget_camp(int budget_camp) {
        this.budget_camp = budget_camp;
    }

    public String getDate_debut_camp() {
        return date_debut_camp;
    }

    public void setDate_debut_camp(String date_debut_camp) {
        this.date_debut_camp = date_debut_camp;
    }

    public String getDate_fin_camp() {
        return date_fin_camp;
    }

    public void setDate_fin_camp(String date_fin_camp) {
        this.date_fin_camp = date_fin_camp;
    }

    public statut_camp getStatut_camp() {
        return statut_camp;
    }

    public void setStatut_camp(statut_camp statut_camp) {
        this.statut_camp = statut_camp;
    }

    public publicites getPublicite() {
        return publicite;
    }

    public void setPublicite(publicites publicite) {
        this.publicite = publicite;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "campagnes_publicitaires{" +
                "id_camp=" + id_camp +
                ", nom_camp='" + nom_camp + '\'' +
                ", budget_camp=" + budget_camp +
                ", date_debut_camp='" + date_debut_camp + '\'' +
                ", date_fin_camp='" + date_fin_camp + '\'' +
                ", statut_camp=" + statut_camp +
                ", publicite=" + publicite +
                '}';
    }
}