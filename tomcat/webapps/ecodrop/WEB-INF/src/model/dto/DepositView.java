package model.dto;

import java.sql.Date;

// Vue enrichie demandée lors de : "GET /deposits : fournit toutes les informations sur les dépots (nom du déchet et adresse du point).""
public class DepositView extends Deposit {
    public String nomDechet;
    public String adressePoint;

    public DepositView(int id, int userId, int pointId, int wasteTypeId, double poids, Date datedepot, boolean collecte,
            String nomDechet, String adressePoint) {
        super(id, userId, pointId, wasteTypeId, poids, datedepot, collecte);
        this.nomDechet = nomDechet;
        this.adressePoint = adressePoint;
    }
}
