package com.java.koffy.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
    public List<Map<String, String>> statement(String query) {
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return constructSelectStatementResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, String>> statement(String query, String... params) {
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            constructStatementParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            return constructSelectStatementResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int dataManipulation(String query, String... params) {
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            constructStatementParameters(statement, params);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void constructStatementParameters(PreparedStatement statement, String... params) throws SQLException {
        IntStream.range(0, params.length)
                .forEach(i -> {
                    try {
                        statement.setObject(i + 1, params[i]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private List<Map<String, String>> constructSelectStatementResult(ResultSet resultSet) {
        try {
            List<Map<String, String>> resultList = new ArrayList<>();
            Map<String, String> queryMap = new HashMap<>();
           while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String columnValue = resultSet.getString(columnName);
                    queryMap.put(columnName, columnValue);
                }
                resultList.add(queryMap);
            }
            return resultList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
