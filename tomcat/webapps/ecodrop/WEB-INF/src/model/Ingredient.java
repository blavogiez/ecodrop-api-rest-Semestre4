package model;

public class Ingredient {
    public int id;
    public String nom;
    public int prix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public Ingredient(int id, String nom, int prix) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Ingredient [id=" + id + ", nom=" + nom + ", prix=" + prix + "]";
    }

    public Ingredient() {

    }

}
