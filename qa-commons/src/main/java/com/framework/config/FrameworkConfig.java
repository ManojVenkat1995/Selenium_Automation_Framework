package com.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FrameworkConfig {

    private static final Properties properties = new Properties();
    private static boolean isLoaded = false;

    public static void load(String env) {
        if (isLoaded) return;

        String fileName = "config/" + env + ".properties";
        try (InputStream input = FrameworkConfig.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            properties.load(input);
            isLoaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Config key not found: " + key);
        }
        return value.trim();
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? value.trim() : defaultValue;
    }
}
