package com.albion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection instance;

    public static Connection get() throws SQLException {
        System.out.println("DEBUG: Abrindo nova conexão com o banco...");
        if (instance == null || instance.isClosed() || !instance.isValid(2)) {
            instance = DriverManager.getConnection(
                    Config.get("db.url"),
                    Config.get("db.user"),
                    Config.get("db.password")
            );
            System.out.println("DEBUG: Conexão OK - " + instance.getMetaData().getURL());
        }
        return instance;
    }
}