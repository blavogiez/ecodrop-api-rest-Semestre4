package model.dto;

public class CollectionPoint {

    private int id;
    private String addresse;
    private int capaciteMax;

    public CollectionPoint(int id, String addresse, int capaciteMax) {
        this.id = id;
        this.addresse = addresse;
        this.capaciteMax = capaciteMax;
    }

    public CollectionPoint() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
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
                ", addresse='" + addresse + '\'' +
                ", capaciteMax=" + capaciteMax +
                '}';
    }
}
