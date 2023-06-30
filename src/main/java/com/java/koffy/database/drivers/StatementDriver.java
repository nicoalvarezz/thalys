package com.java.koffy.database.drivers;

import java.util.List;
import java.util.Map;

public interface StatementDriver {

    List<Map<String, String>> statement(String query, String... params);

    int dataManipulation(String query, String... params);
}
