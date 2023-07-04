package com.java.koffy.database;

import com.java.koffy.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection conn;

    private String url;

    private String user;

    private String password;

    public void setDatabaseCredentials(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConn() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during database connection", e);
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during database connection closure", e);
        }
    }
}
