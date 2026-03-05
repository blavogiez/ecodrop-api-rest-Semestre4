package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.WasteType;
import utils.DS;

public class WasteTypeDAOPostgres implements WasteTypeDAO {
    static final DS DS = new DS();

    public WasteType findById(int id) {
        WasteType wasteType = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from WasteType where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                int prix = rs.getInt("prix");

                wasteType = new WasteType(id, nom, prix);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return wasteType;
    }

    public List<WasteType> findAll() {
        List<WasteType> lesWasteTypes = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select * from WasteType";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                double pointsPerKilo = rs.getDouble("pointsPerKilo");

                WasteType wasteType = new WasteType(id, nom, pointsPerKilo);
                lesWasteTypes.add(wasteType);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return lesWasteTypes;
    }

    public boolean add(WasteType wasteType) {
        try (Connection con = DS.getConnection()) {
            String query = "insert into WasteType(id,nom,pointsPerKilo) values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, wasteType.getId());
            ps.setString(2, wasteType.getNom());
            ps.setDouble(3, wasteType.getPointsPerKilo());

            System.out.println(ps);
            ps.executeUpdate();
            return true ;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false ;
    }

    public boolean delete(WasteType wasteType) {
        try (Connection con = DS.getConnection()) {
            String query = "delete from WasteType where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, wasteType.getId());

            System.out.println(ps);
            ps.executeUpdate();
            return true ;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false ;
    }
}
