package model.dto;

public class Deposit {
    int id;
    int userId;
    int pointId;
    int wasteTypeId;
    double poids;

    public Deposit(int id, int userId, int pointId, int wasteTypeId, double poids) {
        this.id = id;
        this.userId = userId;
        this.pointId = pointId;
        this.wasteTypeId = wasteTypeId;
        this.poids = poids;
    }

    public Deposit() {
    }

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

    @Override
    public String toString() {
        return "DepositDAO{" +
                "id=" + id +
                ", userId=" + userId +
                ", pointId=" + pointId +
                ", wasteTypeId=" + wasteTypeId +
                ", poids=" + poids +
                '}';
    }
}
