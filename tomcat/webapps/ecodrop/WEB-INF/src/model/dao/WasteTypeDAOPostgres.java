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
                double pointsPerKilo = rs.getDouble("pointsPerKilo");

                wasteType = new WasteType(id, nom, pointsPerKilo);
                System.out.println(wasteType);
            }
        } catch (Exception e) {
            System.err.println("Could not  find Waste Type with id " + id + " : " + e.getMessage());
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
            System.err.println("Could not retrieve all Waste Types : " + e.getMessage());
        }

        return lesWasteTypes;
    }

    public boolean add(WasteType wasteType) {
        try (Connection con = DS.getConnection()) {
            String query = "insert into WasteType(nom,pointsPerKilo) values(?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, wasteType.getNom());
            ps.setDouble(2, wasteType.getPointsPerKilo());

            System.out.println(ps);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Could not add Waste Type " + wasteType + " : " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try (Connection con = DS.getConnection()) {
            String query = "delete from WasteType where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            System.out.println(ps);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Could not delete Waste Type at id : " + id + " : " + e.getMessage());
        }
        return false;
    }

    // le WasteType à l'ID existant devient le wasteType en argument
    public WasteType update(int targetId, WasteType wasteType) {
        try (Connection con = DS.getConnection()) {
            String query = "update WasteType set nom = ?, pointsPerKilo = ? where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            // arguments à modifier
            ps.setString(1, wasteType.getNom());
            ps.setDouble(2, wasteType.getPointsPerKilo());

            // cible à changer
            ps.setInt(3, targetId);

            System.out.println(ps);
            boolean hasChanged = (1 == ps.executeUpdate());

            if (hasChanged) {
                System.out.println(wasteType);
                return wasteType;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
