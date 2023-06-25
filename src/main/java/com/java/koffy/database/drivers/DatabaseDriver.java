package com.java.koffy.database.drivers;

import java.sql.SQLException;
import java.util.Map;

public interface DatabaseDriver {

    void connect(String url, String user, String password) throws SQLException;

    void close() throws SQLException;

    Map<String, String> statement(String query, String... params) throws SQLException;

    Map<String, String> statement(String query) throws SQLException;

    int dataManipulation(String query, String... params);
}
