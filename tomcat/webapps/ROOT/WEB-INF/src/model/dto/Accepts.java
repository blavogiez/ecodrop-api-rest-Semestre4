package model.dto;

public class Accepts {
    private int pointsId;
    private int wasteTypesId;

    public Accepts(int pointsId, int wasteTypesId) {
        this.pointsId = pointsId;
        this.wasteTypesId = wasteTypesId;
    }

    public Accepts() {}

    public int getPointsId() {
        return pointsId;
    }

    public void setPointsId(int pointsId) {
        this.pointsId = pointsId;
    }

    public int getWasteTypesId() {
        return wasteTypesId;
    }

    public void setWasteTypesId(int wasteTypesId) {
        this.wasteTypesId = wasteTypesId;
    }

    @Override
    public String toString() {
        return "Accepts{" +
                "pointsId=" + pointsId +
                ", wasteTypesId=" + wasteTypesId +
                '}';
    }
}
