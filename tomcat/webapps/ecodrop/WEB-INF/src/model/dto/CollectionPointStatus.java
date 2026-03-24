package model.dto;

public class CollectionPointStatus {
    public int id;
    public String adresse;
    public double taux;
    public boolean full;

    public CollectionPointStatus(int id, String adresse, double taux) {
        this.id = id;
        this.adresse = adresse;
        this.taux = taux;
        this.full = taux > 80.0;
    }
}
