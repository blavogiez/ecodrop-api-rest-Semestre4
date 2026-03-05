package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class LoginJdbc {
    static final DS DS = new DS();

    public static boolean userExists(String login, String pwd) {
        try (Connection con = DS.getConnection()) {
            String query = "select 1 from users where login=? and pwd=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, pwd);
            System.out.println(ps);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public static boolean verifToken(String token) {
        try (Connection con = DS.getConnection()) {
            String query = "select 1 from users where token=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, token);
            System.out.println(ps);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public static boolean storeUUID(String login, UUID token) {
        String chaineUUID = token.toString();
        try (Connection con = DS.getConnection()) {
            String query = "update users set token=? where login=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, chaineUUID);
            ps.setString(2, login);

            System.out.println(ps);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }
}
