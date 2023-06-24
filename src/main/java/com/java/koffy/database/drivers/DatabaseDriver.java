package com.java.koffy.database.drivers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseDriver {

    void connect(String url, String user, String password) throws SQLException;

    void close() throws SQLException;

    Map<String, String> statement(String query, List<Object> parameters) throws SQLException;

    Map<String, String> statement(String query) throws SQLException;
}
