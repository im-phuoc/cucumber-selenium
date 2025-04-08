package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private final Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    public ConfigManager() {
        properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try(InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
            } else {
                props.setProperty("browser", "chrome");
                props.setProperty("implicitWait", "10");
                props.setProperty("pageLoadTimeout", "30");
                props.setProperty("baseUrl", "https://spring-auth.vercel.app");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait", "10"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("pageLoadTimeout", "30"));
    }

    public String getBaseUrl() {
        return properties.getProperty("baseUrl", "https://spring-auth.vercel.app");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
