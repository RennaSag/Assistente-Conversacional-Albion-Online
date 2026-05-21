package com.albion;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = Config.class.getResourceAsStream("/config.properties")) {
            if (in == null) throw new RuntimeException("config.properties não encontrado!");
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}