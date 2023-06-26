package com.java.koffy.database;

import com.java.koffy.container.Container;
import com.java.koffy.database.drivers.JBDCDriver;
import com.java.koffy.database.drivers.ORMDriver;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Database {

    private static ORMDriver ormDriver = new ORMDriver();
    private static JBDCDriver jbdcDriver = new JBDCDriver();

    public static List<Map<String, String>> selectStatement(String query, String... params) {
        return jbdcDriver.statement(query, params);
    }

    public static List<Map<String, String>> selectStatement(String query) {
        return jbdcDriver.statement(query);
    }

    public static int insertStatement(String query, String... params) {
        return jbdcDriver.dataManipulation(query, params);
    }

    public static int updateStatement(String query, String... params) {
        return jbdcDriver.dataManipulation(query, params);
    }

    public static int deleteStatement(String query, String... params) {
        return jbdcDriver.dataManipulation(query, params);
    }

    public static void save(Object object) throws SQLException {
        ormDriver.save(object);
    }

    public static Object getById(Class<?> clazz, int id) throws SQLException {
        return ormDriver.getById(clazz, id);
    }

    public static List<Object> getAll(Class<?> clazz) throws SQLException {
        return ormDriver.getAll(clazz);
    }
}
