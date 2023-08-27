package com.github.nicoalvarezz.thalys.config;

import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The ConfigAccessor class serves as an interface for accessing application configurations
 * stored in the properties file. These configurations are user-defined settings that interact
 * with the framework's behavior and functionalities.
 */
public class AppConfig {

    private String appName;

    private String appPort;

    private String basePackage;

    private static Logger LOGGER  = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig() {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
            appName = properties.getProperty("thalys.app.name");
            appPort = properties.getProperty("thalys.app.port");
            basePackage = properties.getProperty("thalys.app.basePackage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getAppPort() {
        return appPort;
    }

    public String getBasePackage() {
        return basePackage;
    }
}
