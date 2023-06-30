package com.java.koffy.database;

import java.util.List;
import java.util.Map;

public class DBStatement {

    private static SQLStatement sqlStatement = new SQLStatement();

    public static List<Map<String, String>> selectStatement(String query, String... params) {
        return sqlStatement.statement(query, params);
    }

    public static int insertStatement(String query, String... params) {
        return sqlStatement.dataManipulation(query, params);
    }

    public static int updateStatement(String query, String... params) {
        return sqlStatement.dataManipulation(query, params);
    }

    public static int deleteStatement(String query, String... params) {
        return sqlStatement.dataManipulation(query, params);
    }
}
