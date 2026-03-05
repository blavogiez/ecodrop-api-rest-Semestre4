package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DS {
    static final String url = "jdbc:postgresql://psqlserv/but2";
    static final String user = "baptistelavogiezetu";
    static final String password = "jeanjean";

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}