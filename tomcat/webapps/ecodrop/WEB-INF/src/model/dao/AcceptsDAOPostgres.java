package model.dao;

import model.dto.Accepts;
import model.dto.Accepts;
import utils.DS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AcceptsDAOPostgres implements AcceptsDAO {
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

                Accepts accepts = new Accepts(pointId, wasteTypeId);
                acceptsList.add(accepts);
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve collection points : " + e.getMessage());
        }

        return acceptsList;
    }

    public boolean add(Accepts accepts) {
        try (Connection con = DS.getConnection()) {
            String query = "insert into Accepts(pointsId, wasteTypeId) values(?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, accepts.getPointsId());
            ps.setInt(2, accepts.getWasteTypesId());

            System.out.println(ps);
            ps.executeUpdate();
            return true ;
        } catch (Exception e) {
            System.err.println("Could not add Accepts " + accepts + " : " + e.getMessage());
        }
        return false ;
    }

    public boolean delete(int pointId, int wasteTypeId) {
        try (Connection con = DS.getConnection()) {
            String query = "delete from Accepts where pointsId = ? AND  wasteTypeId = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, pointId);
            ps.setInt(2, pointId);

            System.out.println(ps);
            ps.executeUpdate();
            return true ;
        } catch (Exception e) {
            System.err.println("Could not delete Accepts [id:" + pointId + "," + wasteTypeId + "] : " + e);
        }
        return false ;
    }
}
