package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.CollectionPoint;
import model.dto.CollectionPointStatus;
import model.dto.Deposit;
import model.dto.WasteType;
import utils.DS;

public class CollectionPointDAOPostgres implements CollectionPointDAO {
    private static final DS DS = new DS();

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
                double capaciteMax = rs.getDouble("capaciteMax");

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
                double capaciteMax = rs.getDouble("capaciteMax");

                CollectionPoint collectionPoint = new CollectionPoint(id, adresse, capaciteMax);
                pointsList.add(collectionPoint);
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve collection points : " + e.getMessage());
        }

        return pointsList;
    }

    // liste les points dont l'occupation est > $SEUIL_PARAMETRE
    public List<CollectionPoint> getOccupatedPointsAboveThreshold(int threshold) {
        List<CollectionPoint> pointsList = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select cp.* from CollectionPoint cp left join Deposit dp on dp.pointId = cp.id and (dp.collecte is not true) group by cp.id, cp.adresse, cp.capaciteMax having (coalesce(sum(dp.poids), 0) / cp.capaciteMax * 100) > ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, threshold);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String adresse = rs.getString("adresse");
                double capaciteMax = rs.getDouble("capaciteMax");

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
            String query = "insert into CollectionPoint(id,adresse,capaciteMax) values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, collectionPoint.getId());
            ps.setString(2, collectionPoint.getAdresse());
            ps.setDouble(3, collectionPoint.getCapaciteMax());

            System.out.println(ps);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Could not add Collection Point " + collectionPoint + " : " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try (Connection con = DS.getConnection()) {
            String query = "delete from accepts where pointId = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            System.out.println(ps);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Could not delete waste types in Collection Point [id:" + id + "] : " + e);
        }
        return false;
    }

    public List<WasteType> getAcceptedWasteTypes(int collectionPointId) {
        List<WasteType> acceptedWasteTypes = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select wt.* from wastetype wt join accepts ac on ac.wastetypeid=wt.id where ac.pointid=?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, collectionPointId);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                double pointsPerKilo = rs.getDouble("pointsPerKilo");

                WasteType wasteType = new WasteType(id, nom, pointsPerKilo);
                acceptedWasteTypes.add(wasteType);
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve all Waste Types : " + e.getMessage());
        }

        return acceptedWasteTypes;
    }

    public List<Deposit> deleteAllDepositsFromPoint(int collectionPointId) {
        List<Deposit> deposits = new ArrayList<>();

        try (Connection con = DS.getConnection()) {

            String query = "select * from Deposit where pointId=? and (collecte is not true)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, collectionPointId);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int pointId = rs.getInt("pointId");
                int wasteTypeId = rs.getInt("wasteTypeId");
                double poids = rs.getDouble("poids");
                Date datedepot = rs.getDate("datedepot");
                String collecte = rs.getString("collecte");

                deposits.add(new Deposit(id, userId, pointId, wasteTypeId, poids, datedepot, collecte));
            }

            query = "update Deposit set collecte=true where pointId=?";
            ps = con.prepareStatement(query);

            ps.setInt(1, collectionPointId);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Deletion or retrieval of the deposits failed : " + e.getMessage());
        }

        return deposits;
    }

    public CollectionPointStatus getStatus(int id) {
        try (Connection con = DS.getConnection()) {
            String query =
                "select cp.id, cp.adresse, coalesce(sum(dp.poids), 0) / cp.capaciteMax * 100 as taux " +
                "from CollectionPoint cp " +
                "left join Deposit dp on dp.pointId = cp.id and dp.collecte is not true " +
                "where cp.id = ? " +
                "group by cp.id, cp.adresse, cp.capaciteMax";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CollectionPointStatus(rs.getInt("id"), rs.getString("adresse"), rs.getDouble("taux"));
            }
        } catch (Exception e) {
            System.err.println("Could not get status for Collection Point [id:" + id + "] : " + e.getMessage());
        }
        return null;
    }

    public CollectionPoint update(CollectionPoint updated) {
        try (Connection con = DS.getConnection()) {
            String query = "update CollectionPoint set adresse = ?, capaciteMax = ? where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, updated.getAdresse());
            ps.setDouble(2, updated.getCapaciteMax());

            ps.setInt(3, updated.getId());

            System.out.println(ps);
            ps.executeUpdate();
            return updated;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
