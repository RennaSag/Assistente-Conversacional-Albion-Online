package com.albion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection instance;

    public static Connection get() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(
                    Config.get("db.url"),
                    Config.get("db.user"),
                    Config.get("db.password")
            );
        }
        return instance;
    }
}