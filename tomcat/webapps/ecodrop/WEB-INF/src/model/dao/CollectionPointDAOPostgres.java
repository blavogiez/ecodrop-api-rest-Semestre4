package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.CollectionPoint;
import utils.DS;

public class CollectionPointDAOPostgres implements CollectionPointDAO {
    static final DS DS = new DS();

    public CollectionPoint findById(int id) {
        CollectionPoint collectionPoint = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from CollectionPoint where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String adresse = rs.getString("adresse");
                int capaciteMax = rs.getInt("capaciteMax");

                collectionPoint = new CollectionPoint(id, adresse, capaciteMax);
            }
        } catch (Exception e) {
            System.err.println("Could not find Collection Point with id " + id + " : " + e.getMessage());
        }

        return collectionPoint;
    }

    public List<CollectionPoint> findAll() {
        List<CollectionPoint> pointsList = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select * from CollectionPoint";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String adresse = rs.getString("adresse");
                int capaciteMax = rs.getInt("capaciteMax");

                CollectionPoint collectionPoint = new CollectionPoint(id, adresse, capaciteMax);
                pointsList.add(collectionPoint);
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve collection points : " + e.getMessage());
        }

        return pointsList;
    }

    public boolean add(CollectionPoint collectionPoint) {
        try (Connection con = DS.getConnection()) {
            String query = "insert into CollectionPoint(id,nom,capaciteMax) values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, collectionPoint.getId());
            ps.setString(2, collectionPoint.getAdresse());
            ps.setDouble(3, collectionPoint.getCapaciteMax());

            System.out.println(ps);
            ps.executeUpdate();
            return true ;
        } catch (Exception e) {
            System.err.println("Could not add Collection Point " + collectionPoint + " : " + e.getMessage());
        }
        return false ;
    }

    public boolean delete(int id) {
        try (Connection con = DS.getConnection()) {
            String query = "delete from CollectionPoint where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            System.out.println(ps);
            ps.executeUpdate();
            return true ;
        } catch (Exception e) {
            System.err.println("Could not delete Collection Point [id:" + id + "] : " + e);
        }
        return false ;
    }
}
