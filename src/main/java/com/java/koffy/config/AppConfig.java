package com.java.koffy.config;

import com.sun.tools.javac.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The ConfigAccessor class serves as an interface for accessing application configurations
 * stored in the properties file. These configurations are user-defined settings that interact
 * with the framework's behavior and functionalities.
 */
public class AppConfig {

    private String appName = "";

    private int appPort;

    private String basePackage = "";

    public AppConfig() {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
            appName = properties.getProperty("kinetic.app.name");
            appPort = Integer.parseInt(properties.getProperty("kinetic.app.port"));
            basePackage = properties.getProperty("kinetic.app.basePackage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAppName() {
        return appName;
    }

    public int getAppPort() {
        return appPort;
    }

    public String getBasePackage() {
        return basePackage;
    }
}
