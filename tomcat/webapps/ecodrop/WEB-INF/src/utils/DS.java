package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DS {
    // à l'IUT : psqlserv est dans /etc/hosts → DB_HOST non défini → fallback "psqlserv"
    // en Docker : DB_HOST=ecodrop-db (alias réseau) pour éviter le conflit /etc/hosts
    static final String url = "jdbc:postgresql://" + System.getenv().getOrDefault("DB_HOST", "psqlserv") + "/but2";
    static final String user = "baptistelavogiezetu";
    static final String password = "jeanjean";

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}