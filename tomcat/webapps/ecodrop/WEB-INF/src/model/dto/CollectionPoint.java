package model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class CollectionPoint {

    private int id;
    private String adresse;
    private double capaciteMax;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<WasteType> wasteTypes;

    public CollectionPoint(int id, String adresse, double capaciteMax) {
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

    public double getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(double capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public List<WasteType> getWasteTypes() {
        return wasteTypes;
    }

    public void setWasteTypes(List<WasteType> wasteTypes) {
        this.wasteTypes = wasteTypes;
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
