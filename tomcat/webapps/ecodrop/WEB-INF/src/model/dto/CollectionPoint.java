package model.dto;

public class CollectionPoint {

    private int id;
    private String adresse;
    private int capaciteMax;

    public CollectionPoint(int id, String adresse, int capaciteMax) {
        this.id = id;
        this.adresse = adresse;
        this.capaciteMax = capaciteMax;
    }

    public CollectionPoint() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    @Override
    public String toString() {
        return "CollectionPoint{" +
                "id=" + id +
                ", addresse='" + adresse + '\'' +
                ", capaciteMax=" + capaciteMax +
                '}';
    }
}
