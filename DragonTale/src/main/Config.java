package main;

import java.io.*;
import java.util.Properties;

/**
 * Handles the connection configuration
 */
public class Config {
    public static final String DEFAULT_PORT = "8888";
    public static final String DEFAULT_HOST = "localhost";
    public static final String configFilePath = "game.conf";
    private static Properties conf = null;

    /**
     * gets the configuration specified by key with the specified default value
     *
     * @param key what property we need
     * @param defaultValue the default value if the key doesn't exists
     * @return the property if it exists otherwise defaultValue is returned
     */
    public static String getConfig(String key, String defaultValue) {
        if (conf != null && conf.containsKey(key)) {
            return conf.getProperty(key);
        }
        return defaultValue;
    }

    /**
     * Sets the given data to configuration
     *
     * @param key the key to set the data to
     * @param data the data to set
     */
    public static void setConfig(String key, String data) {
        if (conf != null) {
            conf.setProperty(key, data);
        }
    }

    /**
     * Writes the configuration to file
     *
     * @throws IOException if we can't access the file
     */
    public static void writeConfig() throws IOException {
        if (conf != null) {
            FileWriter writer = new FileWriter(configFilePath);

            conf.store(writer, "");
            writer.close();
        }
    }

    /**
     * Loads configuration from file
     */
    static void loadConfig() {
        File confFile = new File(configFilePath);
        if (!confFile.exists()) {
            System.out.println("game.conf not found, creating...");
            try {
                confFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (InputStream input = new FileInputStream(configFilePath)) {
            conf = new Properties();
            conf.load(input);
            System.out.println("configurations loaded successfully");
        }
        catch (IOException ex) {
            System.out.println("configurations failed to load");
        }
    }
}
