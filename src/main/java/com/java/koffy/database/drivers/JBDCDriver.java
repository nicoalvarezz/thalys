package com.java.koffy.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JBDCDriver implements DatabaseDriver {

    Connection conn;

    @Override
    public void connect(String url, String user, String password) {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> statement(String query) {
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return constructQueryResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> statement(String query, List<Object> parameters) {
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            int resultSet = statement.executeUpdate();
            return new HashMap<>() {{
                put("Updated row(s)", String.valueOf(resultSet));
            }};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> constructQueryResult(ResultSet resultSet) {
        try {
            Map<String, String> queryMap = new HashMap<>();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String columnValue = resultSet.getString(columnName);
                    queryMap.put(columnName, columnValue);
                }
            }
            return queryMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
