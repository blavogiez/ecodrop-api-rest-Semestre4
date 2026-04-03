package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Role;
import utils.DS;

public class AuthDAOPostgres implements AuthDAO {

    private static final DS DS = new DS();

    @Override
    public Role getRole(String username) {
        try (Connection con = DS.getConnection()) {
            String sql = "SELECT role FROM users WHERE login=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Role.valueOf(rs.getString("role"));
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve role for user " + username + " : " + e.getMessage());
        }
        return Role.valueOf("UNKNOWN");
    }

    @Override
    public boolean credentialsReferToExistingAccount(String username, String password) {
        try (Connection con = DS.getConnection()) {
            String sql = "SELECT 1 FROM users WHERE login=? and password=encode(digest(?, 'sha256'), 'hex')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Could not retrieve role for user " + username + " : " + e.getMessage());
        }
        return false;
    }

}
