package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DS {
    // pour tester quand on est pas à l'iut, on bind psqlserv à localhost dans /etc/hosts
    
    static final String url = "jdbc:postgresql://psqlserv/but2";
    static final String user = "baptistelavogiezetu";
    static final String password = "jeanjean";

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}