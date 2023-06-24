package com.java.koffy.database;

import com.java.koffy.container.Container;
import com.java.koffy.database.drivers.JBDCDriver;

import java.util.List;
import java.util.Map;

public class Database {

    public static Map<String, String> statement(String query, List<Object> parameters) {
        return Container.resolve(JBDCDriver.class).statement(query, parameters);
    }

    public static Map<String, String> statement(String query) {
        return Container.resolve(JBDCDriver.class).statement(query);
    }
}
