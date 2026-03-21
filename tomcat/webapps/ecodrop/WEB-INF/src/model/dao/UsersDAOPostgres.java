package model.dao;

import model.Role;
import model.dto.Users;
import utils.DS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsersDAOPostgres implements UsersDAO {

    private static final DS DS = new DS();

    @Override
    public List<Users> findAll() {
        List<Users> users = new ArrayList<>();

        try (Connection con = DS.getConnection()) {
            String query = "select * from Users";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String login = rs.getString("login");
                String password = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role"));

                users.add(new Users(id, login, password, role));
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve all Users " + " : " + e.getMessage());
        }

        return users;
    }

    @Override
    public Users findById(int id) {
        Users users = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from Users where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String login = rs.getString("login");
                String password = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role"));

                users = new Users(id, login, password, role);
            }
        } catch (Exception e) {
            System.err.println("Could not find User with id " + id + " : " + e.getMessage());
        }

        return users;
    }

    @Override
    public Users update(int id, Users updated) {
        try (Connection con = DS.getConnection()) {
            String query = "update users SET login = ?, password = ?, role = ? where id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, updated.getLogin());
            ps.setString(2, updated.getPassword());
            ps.setString(3, updated.getRole().toString());
            ps.setInt(4, id);

            System.out.println(ps);
            ps.executeUpdate();
            return updated;
        } catch (Exception e) {
            System.err.println("Could not update User with informations " + updated + " : " + e.getMessage());
        }
        return null;
    }

    //select userId, sum(poids) * sum(pointsPerKilo) as score from Deposit join WasteType on Deposit.wasteTypeId=WasteType.id group by userId order by (sum(poids) * sum(pointsPerKilo)) desc limit 10;
    @Override
    public List<Users> findArgumentTopRecyclers(int theLimit) {
        List<Users> theBestNRecyclers = new ArrayList<>();

        try (Connection con = DS.getConnection()) {
            String query = "select Users.*, sum(dp.poids * wt.pointsPerKilo) as score from Users join Deposit dp on Users.id=dp.userId join WasteType wt on dp.wasteTypeId=wt.id group by Users.id order by sum(dp.poids * wt.pointsPerKilo) desc limit ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, theLimit);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String login = rs.getString("login");
                String password = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role"));

                theBestNRecyclers.add(new Users(id, login, password, role));
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve all Users " + " : " + e.getMessage());
        }
        return theBestNRecyclers;
    }

}
