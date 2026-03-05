package model;

public class WasteType {
    private int id ;
    private String nom ;
    private double pointsPerKilo ;

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

    public double getPointsPerKilo() {
        return pointsPerKilo;
    }

    public void setPointsPerKilo(double pointsPerKilo) {
        this.pointsPerKilo = pointsPerKilo;
    }

    public WasteType(int id, String nom, double pointsPerKilo) {
        this.id = id;
        this.nom = nom;
        this.pointsPerKilo = pointsPerKilo;
    }

    @Override
    public String toString() {
        return "WasteType [id=" + id + ", nom=" + nom + ", pointsPerKilo=" + pointsPerKilo + "]";
    }
}
