package model.dao;

import utils.DS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.Base64;

public class TokenDAOPostgres implements TokenDAO {

    private static final DS DS = new DS();

    @Override
    public String getOrCreateToken(String login, String password) {

        try (Connection con = DS.getConnection()){

            String sql = "SELECT * FROM users WHERE login=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new SQLDataException("User is not present in database");

            byte[] token = Base64.getEncoder().encode((login+":"+password).getBytes());
            char[] tokenChar = new char[token.length];
            for (int i = 0; i < tokenChar.length; i++){
                tokenChar[i] = (char) token[i];
            }

            return String.valueOf(tokenChar);
        } catch (Exception e){
            System.err.println("Could not retrieve or generate token for user " + login + " : " + e.getMessage());
        }
        return "";
    }
}
