package com.java.koffy.database.drivers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseDriver {

    void close() throws SQLException;

    List<Map<String, String>> statement(String query, String... params) throws SQLException;

    List<Map<String, String>> statement(String query) throws SQLException;

    int dataManipulation(String query, String... params);
}
