package com.java.koffy.database;

import com.java.koffy.database.drivers.StatementDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLStatement implements StatementDriver {

    private PreparedStatement statement;

    private SQLQueryHelpers queryHelpers = new SQLQueryHelpers();

    @Override
    public List<Map<String, String>> statement(String query, String... params) {
        try {
            ResultSet resultSet = queryHelpers.constructStatement(query, Arrays.stream(params).toList()).executeQuery();
            return constructSelectStatementResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int dataManipulation(String query, String... params) {
        try {
            return queryHelpers.constructStatement(query, Arrays.stream(params).toList()).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, String>> constructSelectStatementResult(ResultSet resultSet) {
        try {
            List<Map<String, String>> resultList = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> queryMap = new HashMap<>();
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
