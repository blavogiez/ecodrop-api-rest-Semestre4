package model.dto;

import java.sql.Date;

public class Deposit {
    int id;
    int userId;
    int pointId;
    int wasteTypeId;
    double poids;
    Date datedepot;
    String collecte;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getWasteTypeId() {
        return wasteTypeId;
    }

    public void setWasteTypeId(int wasteTypeId) {
        this.wasteTypeId = wasteTypeId;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public Date getDatedepot() {
        return datedepot;
    }

    public void setDatedepot(Date datedepot) {
        this.datedepot = datedepot;
    }

    public String getCollecte() {
        return collecte;
    }

    public void setCollecte(String collecte) {
        this.collecte = collecte;
    }

    public Deposit(int id, int userId, int pointId, int wasteTypeId, double poids, Date datedepot, String collecte) {
        this.id = id;
        this.userId = userId;
        this.pointId = pointId;
        this.wasteTypeId = wasteTypeId;
        this.poids = poids;
        this.datedepot = datedepot;
        this.collecte = collecte;
    }

}
