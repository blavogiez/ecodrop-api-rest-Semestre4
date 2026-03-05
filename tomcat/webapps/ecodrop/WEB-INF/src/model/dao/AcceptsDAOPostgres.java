package model.dao;

import model.dto.Accepts;
import utils.DS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AcceptsDAOPostgres {
    static final DS DS = new DS();

    public List<Accepts> findAll() {
        List<Accepts> acceptsList = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select * from Accepts";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int pointId = rs.getInt("id");
                int wasteTypeId = rs.getInt("wasteTypeId");

                Accepts collectionPoint = new Accepts(pointId, wasteTypeId);
                acceptsList.add(collectionPoint);
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve collection points : " + e.getMessage());
        }

        return acceptsList;
    }
}
