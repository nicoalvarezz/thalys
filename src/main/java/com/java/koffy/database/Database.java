package com.java.koffy.database;

import com.java.koffy.container.Container;
import com.java.koffy.database.drivers.JBDCDriver;

import java.util.Map;

public class Database {

    public static Map<String, String> selectStatement(String query, String... params) {
        return Container.resolve(JBDCDriver.class).statement(query, params);
    }

    public static Map<String, String> selectStatement(String query) {
        return Container.resolve(JBDCDriver.class).statement(query);
    }

    public static int insertStatement(String query, String... params) {
        return Container.resolve(JBDCDriver.class).dataManipulation(query, params);
    }

    public static int updateStatement(String query, String... params) {
        return Container.resolve(JBDCDriver.class).dataManipulation(query, params);
    }

    public static int deleteStatement(String query, String... params) {
        return Container.resolve(JBDCDriver.class).dataManipulation(query, params);
    }
}
