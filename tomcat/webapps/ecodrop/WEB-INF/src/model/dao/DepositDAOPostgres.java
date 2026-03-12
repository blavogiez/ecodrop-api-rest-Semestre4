package model.dao;

import model.dto.Deposit;
import utils.DS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepositDAOPostgres implements DepositDAO {

    private static final DS DS = new DS();

    @Override
    public List<Deposit> findAll() {
        List<Deposit> deposits = new ArrayList<>();

        try (Connection con = DS.getConnection()) {
            String query = "select * from Deposit";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int pointId = rs.getInt("pointId");
                int wasteTypeId = rs.getInt("wasteTypeId");
                double poids = rs.getDouble("poids");

                deposits.add(new Deposit(id, userId, pointId, wasteTypeId, poids));
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve all Deposits " + " : " + e.getMessage());
        }

        return deposits;
    }

    @Override
    public Deposit findById(int id) {
        Deposit deposit = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from Deposit where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                int pointId = rs.getInt("pointId");
                int wasteTypeId = rs.getInt("wasteTypeId");
                double poids = rs.getDouble("poids");

                deposit = new Deposit(id, userId, pointId, wasteTypeId, poids);
            }
        } catch (Exception e) {
            System.err.println("Could not find Deposit with id " + id + " : " + e.getMessage());
        }

        return deposit;
    }

    @Override
    public boolean add(Deposit deposit) {
        try (Connection con = DS.getConnection()) {
            String query = "insert into Deposit(userId, pointId, wasteTypeId, poids) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, deposit.getUserId());
            ps.setInt(2, deposit.getPointId());
            ps.setInt(3, deposit.getWasteTypeId());
            ps.setDouble(4, deposit.getPoids());

            System.out.println(ps);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Could not add Deposit " + deposit + " : " + e.getMessage());
        }
        return false;
    }

    @Override
    public Deposit update(Deposit updated) {
        try (Connection con = DS.getConnection()) {
            String query = "update Deposit set userId = ?, pointId = ?, wasteTypeId = ?, poids = ? where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, updated.getUserId());
            ps.setInt(2, updated.getPointId());
            ps.setInt(3, updated.getWasteTypeId());
            ps.setDouble(4, updated.getPoids());

            System.out.println(ps);
            ps.executeUpdate();
            return updated;
        } catch (Exception e) {
            System.err.println("Could not update Deposit with informations " + updated + " : " + e.getMessage());
        }
        return null;
    }
}
