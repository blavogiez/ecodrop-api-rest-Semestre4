package model.dao;

import utils.DS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoleDAOPostgres implements RoleDAO{

    private static final DS DS = new DS();

    @Override
    public boolean isAdmin(String username) {
        boolean isAdmin = false;

        try (Connection con = DS.getConnection()){
            String sql = "SELECT role FROM users WHERE login=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isAdmin = rs.getString("role").equals("admin");
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve role for user " + username + " : " + e.getMessage());
        }
        return isAdmin;
    }
}
