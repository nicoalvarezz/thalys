package com.java.koffy.config;


import com.java.koffy.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    Properties properties = new Properties();

    private String datasourceUrl = "";

    private String username = "";

    private String password = "";

    public DatabaseConfig() {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
            datasourceUrl = properties.getProperty("kinetic.datasource.url");
            username = properties.getProperty("kinetic.datasource.username");
            password = properties.getProperty("kinetic.datasource.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String datasourceUrl() {
        return datasourceUrl;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
